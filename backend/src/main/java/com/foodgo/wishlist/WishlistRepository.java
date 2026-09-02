package com.foodgo.wishlist;

import com.foodgo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUser(User user);
    Optional<Wishlist> findByUserAndFoodItemId(User user, Long foodItemId);
    boolean existsByUserAndFoodItemId(User user, Long foodItemId);
}
