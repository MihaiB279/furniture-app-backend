package com.back.web.furniture.PythonModel;

import com.back.web.furniture.Domain.Furniture.Furniture;
import lombok.Data;

import java.util.List;

@Data
public class RoomRequest {
    private double budget;
    private List<Furniture> furniture;
    private List<List<GeneratedFurniture>> alreadyGenerated;

}
