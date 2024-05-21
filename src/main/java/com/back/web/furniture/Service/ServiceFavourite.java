package com.back.web.furniture.Service;

import com.back.web.furniture.Domain.Furniture.Favourite;
import com.back.web.furniture.Dto.FavouriteDto;
import com.back.web.furniture.Dto.RoomBackDto;
import com.back.web.furniture.Dto.RoomFrontDto;

import java.util.List;
import java.util.Map;

public interface ServiceFavourite {
    public boolean addToFavourite(Map<String, RoomBackDto> roomBackDtoMap, String username);

    public List<FavouriteDto> getFavourites(String username);

    public void deleteFavoutireItem(String username, FavouriteDto favItem);
}
