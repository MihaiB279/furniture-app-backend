package com.back.web.furniture.Controller;

import com.back.web.furniture.Domain.User.Role;
import com.back.web.furniture.Dto.FavouriteDto;
import com.back.web.furniture.Dto.RoomBackDto;
import com.back.web.furniture.Exceptions.Messages;
import com.back.web.furniture.Service.ServiceFavourite;
import com.back.web.furniture.Service.ServiceFavouriteImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favourite")
public class ControllerFavourites {
    private final ServiceFavourite serviceFavourite;
    @PostMapping("/add")
    public @ResponseBody ResponseEntity<?> addToFavourite(@RequestBody Map<String, RoomBackDto> roomFrontDto){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER)) {
                serviceFavourite.addToFavourite(roomFrontDto, userDetails.getUsername());
                return new ResponseEntity<>("Added to favourites!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Messages.ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public @ResponseBody ResponseEntity<?> getFavourite(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER)) {
                List<FavouriteDto> favourites = serviceFavourite.getFavourites(userDetails.getUsername());
                return new ResponseEntity<>(favourites, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Messages.ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete")
    public @ResponseBody ResponseEntity<?> getFavourite(@RequestBody FavouriteDto favItem){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER)) {
                serviceFavourite.deleteFavoutireItem(userDetails.getUsername(), favItem);
                return new ResponseEntity<>(favItem, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Messages.ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
