package com.back.web.furniture.Service;

import com.back.web.furniture.Domain.Furniture.Furniture;
import com.back.web.furniture.Domain.Furniture.Room;
import com.back.web.furniture.Domain.User.Address;
import com.back.web.furniture.Dto.FurnitureFrontDto;
import com.back.web.furniture.Dto.RoomFrontDto;
import com.back.web.furniture.Exceptions.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceUtils {
    private ServiceUtils() {
        throw new UnsupportedOperationException(Messages.INITIATE_CLASS_ERROR);
    }
    static Furniture fromFrontDtoToFurniture(FurnitureFrontDto furnitureFrontDto){
        Furniture newFurniture = new Furniture();
        newFurniture.setFurnitureType(furnitureFrontDto.getFurnitureType());
        newFurniture.setName(furnitureFrontDto.getName());
        newFurniture.setPrice(furnitureFrontDto.getPrice());
        Map<String, String> details = furnitureFrontDto.getDetails();
        String stringDetails = "";
        for (Map.Entry<String, String> entry : details.entrySet()) {
            String attribute = entry.getKey();
            String detail = entry.getValue();
            stringDetails += '\'' + attribute + ":" + detail + "\', ";
        }
        stringDetails = '[' + stringDetails.substring(0, stringDetails.length() - 2) + ']';
        newFurniture.setDetails(stringDetails);

        return newFurniture;
    }

    public static Room fromFrontRoomDtoToRoom(RoomFrontDto roomFrontDto){
        Room room = new Room();
        room.setBudget(roomFrontDto.getBudget());
        List<FurnitureFrontDto> frontFurnitureDto = roomFrontDto.getFurniture();
        List<Furniture> furniture = new ArrayList<>();

        for (FurnitureFrontDto frontF : frontFurnitureDto) {
            furniture.add(fromFrontDtoToFurniture(frontF));
        }
        room.setFurniture(furniture);
        return room;
    }

    public static void setAddressDetails(Address toAddress, Address fromAddress) {
        toAddress.setCounty(fromAddress.getCounty());
        toAddress.setCity(fromAddress.getCity());
        toAddress.setStreet(fromAddress.getStreet());
        toAddress.setNumber(fromAddress.getNumber());
        toAddress.setBuildingNumber(fromAddress.getBuildingNumber());
        toAddress.setApartmentNumber(fromAddress.getApartmentNumber());
        toAddress.setStairs(fromAddress.getStairs());
    }
}
