package com.back.web.furniture.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoomFrontDto {
    private double budget;
    private List<FurnitureFrontDto> furniture;
    private List<List<FurnitureBackDto>> alreadyGenerated;
}
