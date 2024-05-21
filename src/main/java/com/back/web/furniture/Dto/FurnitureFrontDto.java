package com.back.web.furniture.Dto;

import com.back.web.furniture.Domain.Furniture.FurnitureType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class FurnitureFrontDto {
    private FurnitureType furnitureType;
    private String name;
    private float price;
    private String company;
    private Map<String, String> details;
}
