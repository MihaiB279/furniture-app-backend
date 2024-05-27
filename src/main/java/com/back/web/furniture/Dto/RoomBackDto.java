package com.back.web.furniture.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoomBackDto {
    private double budget;
    private List<FurnitureBackDto> furniture;
}
