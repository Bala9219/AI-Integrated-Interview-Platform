package com.example.AI_Interview_Platform.controller;

import com.example.AI_Interview_Platform.DTO.AuthDtos.*;
import com.example.AI_Interview_Platform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse register(@RequestBody RegisterRequest request){
        String message = authService.register(request.getName(), request.getEmail(), request.getPassword());

        boolean success = message.equals("User registered successfully");
        return new ApiResponse(success, message);
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request){
        boolean ok = authService.login(request.getEmail(), request.getPassword());

        if(ok){
            return new ApiResponse(true,"Login Successful");
        }else{
            return new ApiResponse(false, "Invalid E-Mail or password");
        }
    }
}
