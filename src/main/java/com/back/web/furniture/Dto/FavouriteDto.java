package com.back.web.furniture.Dto;

import com.back.web.furniture.Domain.Furniture.Furniture;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FavouriteDto {
    private String room;
    private FurnitureBackDto furniture;
}
