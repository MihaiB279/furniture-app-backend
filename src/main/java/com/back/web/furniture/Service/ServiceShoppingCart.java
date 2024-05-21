package com.back.web.furniture.Service;

import com.back.web.furniture.Domain.Furniture.ShoppingCart;
import com.back.web.furniture.Dto.FurnitureBackDto;
import com.back.web.furniture.Dto.FurnitureFrontDto;
import com.back.web.furniture.Dto.ShoppingCartDto;

import java.util.List;

public interface ServiceShoppingCart {
    public boolean addToShoppingCart(List<FurnitureBackDto> furnitureFrontDto, String username);

    public List<ShoppingCartDto> getShoppingCart(String username);

    public void deleteShoppingItem(String username, ShoppingCartDto item);
}
