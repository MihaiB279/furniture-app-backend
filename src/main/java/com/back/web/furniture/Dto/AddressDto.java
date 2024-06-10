package com.back.web.furniture.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {
    private String county;
    private String city;
    private String street;
    private int number;
    private int buildingNumber;
    private int apartmentNumber;
    private String stairs;
}
