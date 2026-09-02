package com.foodgo.address;

import com.foodgo.address.dto.AddressDto;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDto toDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setLabel(address.getLabel());
        dto.setAddressLine(address.getAddressLine());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPinCode(address.getPinCode());
        dto.setLatitude(address.getLatitude());
        dto.setLongitude(address.getLongitude());
        dto.setDefault(address.isDefault());
        return dto;
    }
}
