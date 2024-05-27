package com.back.web.furniture.Controller;

import com.back.web.furniture.Domain.User.Role;
import com.back.web.furniture.Dto.*;
import com.back.web.furniture.Exceptions.Messages;
import com.back.web.furniture.Service.ServiceShoppingCart;
import com.back.web.furniture.Service.ServiceShoppingCartImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/shopping")
public class ControllerShoppingCart {
    protected ServiceShoppingCart serviceShoppingCart;
    @Autowired
    public ControllerShoppingCart(ServiceShoppingCartImpl serviceShoppingCart){
        this.serviceShoppingCart = serviceShoppingCart;
    }

    @PostMapping("/add")
    public @ResponseBody ResponseEntity<?> addToShoppingCart(@RequestBody List<FurnitureBackDto> furnitureBackDto){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER)) {
                serviceShoppingCart.addToShoppingCart(furnitureBackDto, userDetails.getUsername());
                return new ResponseEntity<>("Added to shopping cart!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Messages.ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public @ResponseBody ResponseEntity<?> getShoppingCart(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER)) {
                List<ShoppingCartDto> favourites = serviceShoppingCart.getShoppingCart(userDetails.getUsername());
                return new ResponseEntity<>(favourites, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Messages.ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete")
    public @ResponseBody ResponseEntity<?> deleteItem(@RequestBody ShoppingCartDto item){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER)) {
                serviceShoppingCart.deleteShoppingItem(userDetails.getUsername(), item);
                return new ResponseEntity<>(item, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Messages.ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
