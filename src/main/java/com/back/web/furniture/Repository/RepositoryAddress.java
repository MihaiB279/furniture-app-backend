package com.back.web.furniture.Repository;

import com.back.web.furniture.Domain.User.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryAddress extends JpaRepository<Address, Long> {
    public Address findByApartmentNumberAndBuildingNumberAndNumberAndStairsAndStreetAndCityAndCounty(
            int apartmentNumber,
            int buildingNumber,
            int number,
            String stairs,
            String street,
            String city,
            String county
    );
}
