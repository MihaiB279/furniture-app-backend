package com.back.web.furniture.Repository;

import com.back.web.furniture.Domain.Furniture.FurnitureAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RepositoryFurnitureAttributes  extends JpaRepository<FurnitureAttributes, UUID> {
}
