package com.back.web.furniture.Service;

import com.back.web.furniture.Domain.Furniture.*;
import com.back.web.furniture.Dto.*;
import com.back.web.furniture.Repository.RepositoryFavourites;
import com.back.web.furniture.Repository.RepositoryFurniture;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceFavouriteImpl implements ServiceFavourite{
    private final ModelMapper modelMapper;
    private final RepositoryFavourites repositoryFavourites;
    private final RepositoryFurniture repositoryFurniture;
    @Override
    public boolean addToFavourite(Map<String, RoomBackDto> roomBackDtoMap, String username) {
        String name = roomBackDtoMap.keySet().toString();
        name = name.substring(1, name.length() - 1);
        List<FurnitureBackDto> furnitureBackDtosi = roomBackDtoMap.get(name).getFurniture();
        for(FurnitureBackDto furnitureBackDto: furnitureBackDtosi){
            Favourite favourite = new Favourite();
            favourite.setRoom(name);
            favourite.setUsername(username);
            Furniture furniture = repositoryFurniture.findByFurnitureTypeAndNameAndCompany(furnitureBackDto.getFurnitureType(), furnitureBackDto.getName(),furnitureBackDto.getCompany());
            favourite.setFurniture(furniture);
            try {
                repositoryFavourites.save(favourite);
            }
            catch (Exception ex){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<FavouriteDto> getFavourites(String username) {
        List<Favourite> favourites = repositoryFavourites.findAllByUsername(username);
        return favourites.stream()
                .map(item -> modelMapper.map(item, FavouriteDto.class))
                .toList();
    }

    @Override
    public void deleteFavoutireItem(String username, FavouriteDto favItem) {
        Furniture furniture = repositoryFurniture.findByFurnitureTypeAndNameAndCompany(favItem.getFurniture().getFurnitureType(), favItem.getFurniture().getName(), favItem.getFurniture().getCompany());
        Favourite favourite = repositoryFavourites.findByUsernameAndFurnitureAndRoom(username, furniture, favItem.getRoom());
        repositoryFavourites.deleteById(favourite.getId());
    }

}
