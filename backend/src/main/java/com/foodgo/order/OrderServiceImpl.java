package com.foodgo.order;

import com.foodgo.cart.Cart;
import com.foodgo.cart.CartItem;
import com.foodgo.cart.CartService;
import com.foodgo.exception.BadRequestException;
import com.foodgo.exception.ResourceNotFoundException;
import com.foodgo.exception.UnauthorizedException;
import com.foodgo.order.dto.OrderDto;
import com.foodgo.order.dto.PlaceOrderRequest;
import com.foodgo.payment.PaymentService;
import com.foodgo.restaurant.Restaurant;
import com.foodgo.restaurant.RestaurantService;
import com.foodgo.user.User;
import com.foodgo.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private static final BigDecimal TAX_RATE = BigDecimal.valueOf(0.05);
    private static final BigDecimal WELCOME_DISCOUNT = BigDecimal.valueOf(50);
    private static final Set<OrderStatus> CANCELLABLE_STATUSES = EnumSet.of(OrderStatus.PLACED, OrderStatus.CONFIRMED);

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CartService cartService;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final PaymentService paymentService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, CartService cartService,
                             UserService userService, RestaurantService restaurantService, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.cartService = cartService;
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public OrderDto placeOrder(String customerEmail, PlaceOrderRequest request) {
        User customer = userService.getUserEntityByEmail(customerEmail);
        Cart cart = cartService.getEntityByEmail(customerEmail);

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Your cart is empty");
        }

        Restaurant restaurant = cart.getItems().get(0).getFoodItem().getRestaurant();
        for (CartItem cartItem : cart.getItems()) {
            if (!cartItem.getFoodItem().isAvailable()) {
                throw new BadRequestException("Item '" + cartItem.getFoodItem().getName() + "' is no longer available");
            }
            if (!Objects.equals(cartItem.getFoodItem().getRestaurant().getId(), restaurant.getId())) {
                throw new BadRequestException("All items in an order must belong to the same restaurant");
            }
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setDeliveryAddressLine(request.getDeliveryAddressLine());

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setFoodItem(cartItem.getFoodItem());
            orderItem.setFoodItemName(cartItem.getFoodItem().getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getFoodItem().getPrice());

            BigDecimal lineTotal = cartItem.getFoodItem().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            orderItem.setLineTotal(lineTotal);
            subtotal = subtotal.add(lineTotal);

            order.getItems().add(orderItem);
        }

        BigDecimal deliveryFee = restaurant.getDeliveryFee();
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal discount = resolveDiscount(request.getDiscountCode(), subtotal);
        BigDecimal total = subtotal.add(deliveryFee).add(tax).subtract(discount).setScale(2, RoundingMode.HALF_UP);

        order.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        order.setDeliveryFee(deliveryFee.setScale(2, RoundingMode.HALF_UP));
        order.setTax(tax);
        order.setDiscount(discount.setScale(2, RoundingMode.HALF_UP));
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PLACED);

        Order savedOrder = orderRepository.save(order);
        paymentService.createPaymentForOrder(savedOrder, request.getPaymentMethod(), total);
        cartService.clearCart(customerEmail);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getMyOrders(String customerEmail) {
        User customer = userService.getUserEntityByEmail(customerEmail);
        return orderRepository.findByCustomerOrderByCreatedAtDesc(customer).stream().map(orderMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getMyOrderById(String customerEmail, Long orderId) {
        User customer = userService.getUserEntityByEmail(customerEmail);
        Order order = orderRepository.findByIdAndCustomer(orderId, customer)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(String customerEmail, Long orderId) {
        User customer = userService.getUserEntityByEmail(customerEmail);
        Order order = orderRepository.findByIdAndCustomer(orderId, customer)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!CANCELLABLE_STATUSES.contains(order.getStatus())) {
            throw new BadRequestException("This order can no longer be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getRestaurantOrders(String ownerEmail) {
        Restaurant restaurant = restaurantService.getEntityByOwnerEmail(ownerEmail);
        return orderRepository.findByRestaurantOrderByCreatedAtDesc(restaurant).stream().map(orderMapper::toDto).toList();
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(String ownerEmail, Long orderId, OrderStatus status) {
        Restaurant restaurant = restaurantService.getEntityByOwnerEmail(ownerEmail);
        Order order = orderRepository.findByIdAndRestaurant(orderId, restaurant)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new UnauthorizedException("This order's status can no longer be changed");
        }

        order.setStatus(status);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toDto).toList();
    }

    @Override
    @Transactional
    public OrderDto adminUpdateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        return orderMapper.toDto(orderRepository.save(order));
    }

    private BigDecimal resolveDiscount(String discountCode, BigDecimal subtotal) {
        if (discountCode == null || discountCode.isBlank()) {
            return BigDecimal.ZERO;
        }
        if ("FOODGO50".equalsIgnoreCase(discountCode.trim()) && subtotal.compareTo(BigDecimal.valueOf(200)) >= 0) {
            return WELCOME_DISCOUNT;
        }
        return BigDecimal.ZERO;
    }
}
