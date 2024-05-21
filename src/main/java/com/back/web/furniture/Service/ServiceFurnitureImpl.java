package com.back.web.furniture.Service;

import com.back.web.furniture.Domain.Furniture.Furniture;
import com.back.web.furniture.Domain.Furniture.FurnitureAttributes;
import com.back.web.furniture.Domain.Furniture.FurnitureType;
import com.back.web.furniture.Domain.Furniture.Room;
import com.back.web.furniture.Dto.*;
import com.back.web.furniture.Repository.RepositoryFurniture;
import com.back.web.furniture.Repository.RepositoryFurnitureAttributes;
import com.back.web.furniture.Utils.ToPythonScript;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class ServiceFurnitureImpl implements ServiceFurniture {
    private RepositoryFurnitureAttributes repositoryFurnitureAttributes;
    private RepositoryFurniture repositoryFurniture;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public ServiceFurnitureImpl(RepositoryFurnitureAttributes repositoryFurnitureAttributes, RepositoryFurniture repositoryFurniture) {
        this.repositoryFurnitureAttributes = repositoryFurnitureAttributes;
        this.repositoryFurniture = repositoryFurniture;
    }

    @Override
    public Map<String, RoomBackDto> generateFurniture(Map<String, RoomFrontDto> roomsDto) {
        Map<String, RoomBackDto> rooms = new HashMap<>();
        for (String key : roomsDto.keySet()) {
            RoomFrontDto value = roomsDto.get(key);
            List<List<FurnitureBackDto>> alreadyGeneratedDto = value.getAlreadyGenerated();
            List<List<Furniture>> alreadyGenerated = null;
            if(alreadyGeneratedDto!=null) {
                alreadyGenerated = alreadyGeneratedDto.stream()
                        .map(subList ->
                                subList.stream()
                                        .map(dto -> modelMapper.map(dto, Furniture.class))
                                        .toList()
                        )
                        .collect(Collectors.toList());
            }
            Room room = ServiceUtils.fromFrontRoomDtoToRoom(value);
            try {
                Room finishedRoom = ToPythonScript.toGenerateScript(room, alreadyGenerated);
                rooms.put(key, modelMapper.map(finishedRoom, RoomBackDto.class));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return rooms;
    }

    @Override
    public Map<String, Map<String, List<String>>> getFurnitureAttributes() {
        List<FurnitureAttributes> furniture = repositoryFurnitureAttributes.findAll();
        return furniture.stream()
                .collect(Collectors.groupingBy(
                        FurnitureAttributes::getName, // First level grouping by name
                        Collectors.groupingBy(
                                FurnitureAttributes::getAttribute, // Second level grouping by attribute
                                Collectors.mapping(FurnitureAttributes::getValue, Collectors.toList()) // Mapping values to a list
                        )
                ));
    }

    @Override
    public List<FurnitureBackDto> getFurnitureForCompany(String company) {
        List<Furniture> furniture = repositoryFurniture.findAllByCompany(company);
        return furniture.stream()
                .map(item -> modelMapper.map(item, FurnitureBackDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FurnitureBackDto addFurniture(String company, FurnitureFrontDto furnitureFrontDto) {
        Furniture furniture = ServiceUtils.fromFrontDtoToFurniture(furnitureFrontDto);
        furniture.setCompany(company);
        try{
            repositoryFurniture.save(furniture);
            return modelMapper.map(furniture, FurnitureBackDto.class);
        }
        catch (Exception e){
            return null;
        }
    }

    @Override
    public FurnitureBackDto delete(String type, String name, String company) {
        Furniture furniture = repositoryFurniture.findByFurnitureTypeAndNameAndCompany(FurnitureType.valueOf(type), name, company);
        if(furniture != null){
            repositoryFurniture.deleteById(furniture.getId());
            return modelMapper.map(furniture, FurnitureBackDto.class);
        }
        return null;
    }


}
