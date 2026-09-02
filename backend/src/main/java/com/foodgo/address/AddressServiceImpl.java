package com.foodgo.address;

import com.foodgo.address.dto.AddressDto;
import com.foodgo.address.dto.AddressRequest;
import com.foodgo.exception.ResourceNotFoundException;
import com.foodgo.user.User;
import com.foodgo.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserService userService;

    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper,
                               UserService userService) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.userService = userService;
    }

    @Override
    public List<AddressDto> getMyAddresses(String email) {
        User user = userService.getUserEntityByEmail(email);
        return addressRepository.findByUser(user).stream().map(addressMapper::toDto).toList();
    }

    @Override
    public AddressDto addAddress(String email, AddressRequest request) {
        User user = userService.getUserEntityByEmail(email);
        Address address = new Address();
        applyRequest(address, request);
        address.setUser(user);
        return addressMapper.toDto(addressRepository.save(address));
    }

    @Override
    public AddressDto updateAddress(String email, Long addressId, AddressRequest request) {
        User user = userService.getUserEntityByEmail(email);
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        applyRequest(address, request);
        return addressMapper.toDto(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(String email, Long addressId) {
        User user = userService.getUserEntityByEmail(email);
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        addressRepository.delete(address);
    }

    private void applyRequest(Address address, AddressRequest request) {
        address.setLabel(request.getLabel());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPinCode(request.getPinCode());
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());
        address.setDefault(request.isDefault());
    }
}
