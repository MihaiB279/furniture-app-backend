package com.back.web.furniture.Controller;

import com.back.web.furniture.Domain.User.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class ControllerUtils {
    static boolean checkRole(UserDetails userDetails, Role role) {
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) userDetails.getAuthorities();
        return (authorities.stream().anyMatch(auth -> auth.getAuthority().equals(role.toString())));
    }
}
