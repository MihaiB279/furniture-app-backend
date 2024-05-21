package com.back.web.furniture.Domain.Furniture;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "FurnitureTable")
public class Furniture {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private FurnitureType furnitureType;
    @Column
    private String name;
    @Column
    private float price;
    @Column(length = 500)
    private String details;
    @Column
    private String company;
    @OneToMany(mappedBy = "furniture", cascade = CascadeType.REMOVE)
    private Set<Favourite> favourites = new HashSet<>();

    @OneToMany(mappedBy = "furniture", cascade = CascadeType.REMOVE)
    private Set<ShoppingCart> shoppingCarts = new HashSet<>();
}
