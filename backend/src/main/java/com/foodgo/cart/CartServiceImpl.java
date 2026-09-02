package com.foodgo.cart;

import com.foodgo.cart.dto.AddCartItemRequest;
import org.springframework.transaction.annotation.Transactional;
import com.foodgo.cart.dto.CartDto;
import com.foodgo.cart.dto.UpdateCartItemRequest;
import com.foodgo.exception.BadRequestException;
import com.foodgo.exception.ResourceNotFoundException;
import com.foodgo.food.FoodItem;
import com.foodgo.food.FoodItemService;
import com.foodgo.user.User;
import com.foodgo.user.UserService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final UserService userService;
    private final FoodItemService foodItemService;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
                            CartMapper cartMapper, UserService userService, FoodItemService foodItemService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
        this.userService = userService;
        this.foodItemService = foodItemService;
    }

    @Override
    public CartDto getCart(String email) {
        return cartMapper.toDto(getEntityByEmail(email));
    }

    @Override
    public CartDto addItem(String email, AddCartItemRequest request) {
        Cart cart = getEntityByEmail(email);
        FoodItem foodItem = foodItemService.getEntityById(request.getFoodItemId());

        if (!foodItem.isAvailable()) {
            throw new BadRequestException("This food item is currently unavailable");
        }

        CartItem existing = cartItemRepository.findByCartAndFoodItemId(cart, foodItem.getId()).orElse(null);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + request.getQuantity());
            cartItemRepository.save(existing);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setFoodItem(foodItem);
            item.setQuantity(request.getQuantity());
            cart.getItems().add(item);
            cartItemRepository.save(item);
        }

        return cartMapper.toDto(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Override
    public CartDto updateItem(String email, Long cartItemId, UpdateCartItemRequest request) {
        Cart cart = getEntityByEmail(email);
        CartItem item = cartItemRepository.findByIdAndCart(cartItemId, cart)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        return cartMapper.toDto(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Override
    public CartDto removeItem(String email, Long cartItemId) {
        Cart cart = getEntityByEmail(email);
        CartItem item = cartItemRepository.findByIdAndCart(cartItemId, cart)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        return cartMapper.toDto(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Override
    public void clearCart(String email) {
        Cart cart = getEntityByEmail(email);
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public Cart getEntityByEmail(String email) {
        User user = userService.getUserEntityByEmail(email);
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }
}
