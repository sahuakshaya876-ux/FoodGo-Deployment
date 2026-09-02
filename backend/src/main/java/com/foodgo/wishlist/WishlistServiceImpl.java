package com.foodgo.wishlist;

import com.foodgo.exception.ConflictException;
import com.foodgo.exception.ResourceNotFoundException;
import com.foodgo.food.FoodItem;
import com.foodgo.food.FoodItemService;
import com.foodgo.user.User;
import com.foodgo.user.UserService;
import com.foodgo.wishlist.dto.WishlistItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;
    private final UserService userService;
    private final FoodItemService foodItemService;

    public WishlistServiceImpl(
            WishlistRepository wishlistRepository,
            WishlistMapper wishlistMapper,
            UserService userService,
            FoodItemService foodItemService) {

        this.wishlistRepository = wishlistRepository;
        this.wishlistMapper = wishlistMapper;
        this.userService = userService;
        this.foodItemService = foodItemService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<WishlistItemDto> getMyWishlist(String email) {

        User user = userService.getUserEntityByEmail(email);

        return wishlistRepository.findByUser(user)
                .stream()
                .map(wishlistMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public WishlistItemDto addToWishlist(String email, Long foodItemId) {

        User user = userService.getUserEntityByEmail(email);

        FoodItem foodItem = foodItemService.getEntityById(foodItemId);

        if (wishlistRepository.existsByUserAndFoodItemId(user, foodItemId)) {
            throw new ConflictException(
                    "This item is already in your wishlist"
            );
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setFoodItem(foodItem);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);

        return wishlistMapper.toDto(savedWishlist);
    }

    @Override
    @Transactional
    public void removeFromWishlist(String email, Long foodItemId) {

        User user = userService.getUserEntityByEmail(email);

        Wishlist wishlist = wishlistRepository
                .findByUserAndFoodItemId(user, foodItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Wishlist item not found"
                        )
                );

        wishlistRepository.delete(wishlist);
    }
}