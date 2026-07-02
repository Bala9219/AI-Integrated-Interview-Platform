package com.example.AI_Interview_Platform.controller;

import com.example.AI_Interview_Platform.DTO.UserDtos.*;
import com.example.AI_Interview_Platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile/{email}")
    public ResponseEntity<UserResponse> getProfile(@PathVariable String email){
        return ResponseEntity.ok(userService.getUserProfile(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UserRequest request){
        return ResponseEntity.ok(userService.updateUserProfile(request));
    }
}
