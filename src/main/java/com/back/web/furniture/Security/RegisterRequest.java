package com.back.web.furniture.Security;

import com.back.web.furniture.Domain.User.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String password;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Address address;
    private String company;
}
