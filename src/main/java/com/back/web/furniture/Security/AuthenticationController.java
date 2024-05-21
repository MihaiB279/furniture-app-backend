package com.back.web.furniture.Security;

import com.back.web.furniture.Domain.User.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    @PostMapping("/register_normal")
    public ResponseEntity<AuthenticationResponse> registerNormal(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request, Role.USER));
    }

    @PostMapping("/register_company")
    public ResponseEntity<AuthenticationResponse> registerCompany(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request, Role.COMPANY));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request){
        try {
            return ResponseEntity.ok(service.authenticate(request));
        }
        catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials. Please check your username and password.");
        }
    }
}
