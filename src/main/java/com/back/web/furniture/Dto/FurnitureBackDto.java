package com.back.web.furniture.Dto;

import com.back.web.furniture.Domain.Furniture.FurnitureType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FurnitureBackDto {
    private FurnitureType furnitureType;
    private String name;
    private float price;
    private String details;
    private String company;
}
