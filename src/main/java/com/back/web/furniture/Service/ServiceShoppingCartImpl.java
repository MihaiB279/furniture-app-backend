package com.back.web.furniture.Service;

import com.back.web.furniture.Domain.Furniture.Furniture;
import com.back.web.furniture.Domain.Furniture.ShoppingCart;
import com.back.web.furniture.Dto.FurnitureBackDto;
import com.back.web.furniture.Dto.ShoppingCartDto;
import com.back.web.furniture.Repository.RepositoryFurniture;
import com.back.web.furniture.Repository.RepositoryShoppingCart;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceShoppingCartImpl implements ServiceShoppingCart {
    private final ModelMapper modelMapper;
    private final RepositoryShoppingCart repositoryShoppingCart;
    private final RepositoryFurniture repositoryFurniture;

    @Override
    public boolean addToShoppingCart(List<FurnitureBackDto> furnitureBackDto, String username) {
        for (FurnitureBackDto furnitureBackDto1 : furnitureBackDto) {
            ShoppingCart shoppingCart = new ShoppingCart();
            Furniture furniture = repositoryFurniture.findByFurnitureTypeAndNameAndCompany(furnitureBackDto1.getFurnitureType(), furnitureBackDto1.getName(), furnitureBackDto1.getCompany());
            shoppingCart.setUsername(username);
            shoppingCart.setFurniture(furniture);
            try {
                repositoryShoppingCart.save(shoppingCart);
            } catch (Exception ex) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<ShoppingCartDto> getShoppingCart(String username) {
        List<ShoppingCart> shoppingCarts = repositoryShoppingCart.findAllByUsername(username);
        return shoppingCarts.stream()
                .map(item -> modelMapper.map(item, ShoppingCartDto.class))
                .toList();
    }

    @Override
    public void deleteShoppingItem(String username, ShoppingCartDto item) {
        Furniture furniture = repositoryFurniture.findByFurnitureTypeAndNameAndCompany(item.getFurniture().getFurnitureType(), item.getFurniture().getName(), item.getFurniture().getCompany());
        List<ShoppingCart> shoppingCartList = repositoryShoppingCart.findAllByUsernameAndFurniture(username, furniture);
        if (!shoppingCartList.isEmpty()) {
            ShoppingCart shoppingCart = shoppingCartList.get(0);
            repositoryShoppingCart.deleteById(shoppingCart.getId());
        }
    }
}
