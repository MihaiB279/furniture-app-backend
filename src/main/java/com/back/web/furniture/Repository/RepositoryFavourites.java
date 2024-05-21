package com.back.web.furniture.Repository;

import com.back.web.furniture.Domain.Furniture.Favourite;
import com.back.web.furniture.Domain.Furniture.Furniture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RepositoryFavourites extends JpaRepository<Favourite, UUID> {
    public List<Favourite> findAllByUsername(String username);
    public Favourite findByUsernameAndFurnitureAndRoom(String username, Furniture furniture, String rooom);
}
