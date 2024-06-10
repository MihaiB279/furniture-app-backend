package com.back.web.furniture.Service;

import com.back.web.furniture.Dto.FurnitureBackDto;
import com.back.web.furniture.Dto.FurnitureFrontDto;
import com.back.web.furniture.Dto.RoomBackDto;
import com.back.web.furniture.Dto.RoomFrontDto;

import java.util.List;
import java.util.Map;

public interface ServiceFurniture {
    public Map<String, RoomBackDto> generateFurniture(Map<String, RoomFrontDto> roomsDto,  String jwt) throws Exception;
    public Map<String, Map<String, List<String>>> getFurnitureAttributes();
    public List<FurnitureBackDto> getFurnitureForCompany(String company);
    public FurnitureBackDto addFurniture(String company, FurnitureFrontDto furnitureFrontDto);
    public FurnitureBackDto delete(String type, String name, String company);
    public List<String> getCompanies();
}
