package com.back.web.furniture.Domain.Furniture;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "FurnitureAttributesTable")
public class FurnitureAttributes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String name;
    @Column
    private String attribute;
    @Column
    private String value;
}
