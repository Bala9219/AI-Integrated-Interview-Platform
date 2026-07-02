package com.example.AI_Interview_Platform.service;

import com.example.AI_Interview_Platform.DTO.UserDtos.*;
import com.example.AI_Interview_Platform.entity.User;
import com.example.AI_Interview_Platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse updateUserProfile(UserRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));

        user.setName(request.getName());

        if(request.getNewEmail()!=null && !request.getNewEmail().isBlank()){
            user.setEmail(request.getNewEmail());
        }

        User updatedUser = userRepository.save(user);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail()
        );
    }

    public UserResponse getUserProfile(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
