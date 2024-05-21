package com.back.web.furniture.Domain.Furniture;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Room {
    private double budget;
    private List<Furniture> furniture;
}
