package com.back.web.furniture.Controller;

import com.back.web.furniture.Domain.User.Role;
import com.back.web.furniture.Dto.FurnitureBackDto;
import com.back.web.furniture.Dto.FurnitureFrontDto;
import com.back.web.furniture.Dto.RoomBackDto;
import com.back.web.furniture.Dto.RoomFrontDto;
import com.back.web.furniture.Exceptions.Messages;
import com.back.web.furniture.Service.ServiceFurniture;
import com.back.web.furniture.Service.ServiceFurnitureImpl;
import com.back.web.furniture.Service.ServiceUser;
import com.back.web.furniture.Service.ServiceUserImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
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
@RequestMapping("/api/furniture")
public class ControllerFurniture {
    protected ServiceFurniture serviceFurniture;
    protected ServiceUser serviceUser;

    @Autowired
    public ControllerFurniture(ServiceFurnitureImpl serviceFurniture, ServiceUserImpl serviceUser) {
        this.serviceFurniture = serviceFurniture;
        this.serviceUser = serviceUser;
    }

    @PostMapping("/generate")
    public @ResponseBody ResponseEntity<?> generateFurnitureForRooms(@NonNull HttpServletRequest request, @RequestBody Map<String, RoomFrontDto> rooms) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER)) {
                Map<String, RoomBackDto> furnitureGenerated = serviceFurniture.generateFurniture(rooms, request.getHeader("Authorization").substring(7));
                return new ResponseEntity<>(furnitureGenerated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Messages.ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addFurniture")
    public ResponseEntity<?> addFurniture(@RequestBody FurnitureFrontDto furniture){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.COMPANY)) {
                String username = userDetails.getUsername();
                FurnitureBackDto furnitureAdded = serviceFurniture.addFurniture(serviceUser.getUser(username).getCompany(), furniture);
                return new ResponseEntity<>(furnitureAdded, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Messages.ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteFurniture/{type}/{name}")
    public ResponseEntity<?> deleteFurniture(@PathVariable String type, @PathVariable String name){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.COMPANY)) {
                String username = userDetails.getUsername();
                String company = serviceUser.getUser(username).getCompany();
                FurnitureBackDto furnitureDeleted = serviceFurniture.delete(type, name, company);
                if(furnitureDeleted != null) {
                    return new ResponseEntity<>(furnitureDeleted, HttpStatus.OK);
                }
                else return new ResponseEntity<>("Furniture not found", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(Messages.ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getFurnitureCompany")
    public @ResponseBody ResponseEntity<?> getFurnitureForCompany() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.COMPANY)) {
                String username = userDetails.getUsername();
                List<FurnitureBackDto> furniture = serviceFurniture.getFurnitureForCompany(serviceUser.getUser(username).getCompany());
                return new ResponseEntity<>(furniture, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Messages.ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getDetails")
    public @ResponseBody ResponseEntity<?> getFurnitureDetails() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER) || ControllerUtils.checkRole(userDetails, Role.COMPANY)) {
                Map<String, Map<String, List<String>>> furniture = serviceFurniture.getFurnitureAttributes();
                return new ResponseEntity<>(furniture, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Messages.ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
