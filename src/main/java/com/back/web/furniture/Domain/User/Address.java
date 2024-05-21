package com.back.web.furniture.Domain.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "AddressTable")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String county;
    @Column
    private String city;
    @Column
    private String street;
    @Column
    private int number;
    @Column
    private int buildingNumber;
    @Column
    private int apartmentNumber;
    @Column
    private String stairs;
}
