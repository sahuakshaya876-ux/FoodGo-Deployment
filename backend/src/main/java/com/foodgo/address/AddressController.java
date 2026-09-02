package com.foodgo.address;

import com.foodgo.address.dto.AddressDto;
import com.foodgo.address.dto.AddressRequest;
import com.foodgo.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ApiResponse<List<AddressDto>> getMyAddresses(Authentication authentication) {
        return ApiResponse.success("Addresses fetched successfully", addressService.getMyAddresses(authentication.getName()));
    }

    @PostMapping
    public ApiResponse<AddressDto> addAddress(Authentication authentication, @Valid @RequestBody AddressRequest request) {
        return ApiResponse.success("Address added successfully", addressService.addAddress(authentication.getName(), request));
    }

    @PutMapping("/{id}")
    public ApiResponse<AddressDto> updateAddress(Authentication authentication, @PathVariable Long id,
                                                  @Valid @RequestBody AddressRequest request) {
        return ApiResponse.success("Address updated successfully", addressService.updateAddress(authentication.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAddress(Authentication authentication, @PathVariable Long id) {
        addressService.deleteAddress(authentication.getName(), id);
        return ApiResponse.success("Address deleted successfully");
    }
}
