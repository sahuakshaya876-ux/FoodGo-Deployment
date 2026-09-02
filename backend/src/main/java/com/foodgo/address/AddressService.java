package com.foodgo.address;

import com.foodgo.address.dto.AddressDto;
import com.foodgo.address.dto.AddressRequest;

import java.util.List;

public interface AddressService {
    List<AddressDto> getMyAddresses(String email);
    AddressDto addAddress(String email, AddressRequest request);
    AddressDto updateAddress(String email, Long addressId, AddressRequest request);
    void deleteAddress(String email, Long addressId);
}
