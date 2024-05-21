package com.back.web.furniture.Service;

import com.back.web.furniture.Domain.Furniture.Room;
import com.back.web.furniture.Dto.FurnitureBackDto;
import com.back.web.furniture.Dto.FurnitureFrontDto;
import com.back.web.furniture.Dto.RoomBackDto;
import com.back.web.furniture.Dto.RoomFrontDto;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public interface ServiceFurniture {
    public Map<String, RoomBackDto> generateFurniture(Map<String, RoomFrontDto> roomsDto);
    public Map<String, Map<String, List<String>>> getFurnitureAttributes();
    public List<FurnitureBackDto> getFurnitureForCompany(String company);
    public FurnitureBackDto addFurniture(String company, FurnitureFrontDto furnitureFrontDto);

    FurnitureBackDto delete(String type, String name, String company);
}
