package com.back.web.furniture.Repository;

import com.back.web.furniture.Domain.Furniture.Furniture;
import com.back.web.furniture.Domain.Furniture.FurnitureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RepositoryFurniture extends JpaRepository<Furniture, UUID> {
    public List<Furniture> findAllByCompany(String company);
    public Furniture findByFurnitureTypeAndNameAndCompany(FurnitureType furnitureType, String name, String company);
    @Query(value = "SELECT ROW_NUMBER() OVER(ORDER BY id) AS rowNumber FROM furniture_table WHERE furniture_type = :value1 AND name = :value2 AND company = :value3", nativeQuery = true)
    public Long getRowNumber(@Param("value1") String type, @Param("value2") String name, @Param("value3") String company);
}
