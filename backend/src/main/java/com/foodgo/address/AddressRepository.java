package com.foodgo.address;

import com.foodgo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
    Optional<Address> findByIdAndUser(Long id, User user);
}
