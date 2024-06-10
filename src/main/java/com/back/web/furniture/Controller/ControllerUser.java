package com.back.web.furniture.Controller;

import com.back.web.furniture.Domain.User.Role;
import com.back.web.furniture.Dto.UserDto;
import com.back.web.furniture.Service.ServiceUser;
import com.back.web.furniture.Service.ServiceUserImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class ControllerUser {
    private final ServiceUser service;
    @GetMapping("/getProfile")
    public @ResponseBody ResponseEntity<?> getProfile(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER) || ControllerUtils.checkRole(userDetails, Role.COMPANY)) {
                return new ResponseEntity<>(service.getUser(userDetails.getUsername()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/modify")
    public @ResponseBody ResponseEntity<?> editProfile(@RequestBody UserDto user){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER) || ControllerUtils.checkRole(userDetails, Role.COMPANY)) {
                UserDto userDto = service.updateUser(user);
                return new ResponseEntity<>(userDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
