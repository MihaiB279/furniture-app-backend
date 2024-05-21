package com.back.web.furniture.Dto;

import com.back.web.furniture.Domain.User.Address;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Address address;
    private String company;
}
