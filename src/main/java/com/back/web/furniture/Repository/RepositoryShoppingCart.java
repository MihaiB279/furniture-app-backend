package com.back.web.furniture.Repository;

import com.back.web.furniture.Domain.Furniture.Furniture;
import com.back.web.furniture.Domain.Furniture.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RepositoryShoppingCart extends JpaRepository<ShoppingCart, UUID> {
    public List<ShoppingCart> findAllByUsername(String username);
    public ShoppingCart findByUsernameAndFurniture(String username, Furniture furniture);
    public void deleteAllByUsername(String username);
}
