package com.back.web.furniture.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FavouriteDto {
    private String room;
    private FurnitureBackDto furniture;
}
