package com.back.web.furniture.Dto;

import com.back.web.furniture.Domain.Furniture.Furniture;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoomBackDto {
    private double budget;
    private List<FurnitureBackDto> furniture;
}
