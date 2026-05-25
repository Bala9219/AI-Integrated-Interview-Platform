package com.example.AI_Interview_Platform.service;

import com.example.AI_Interview_Platform.entity.User;
import com.example.AI_Interview_Platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public String register(String name, String email, String password){
        if(userRepository.findByEmail(email).isPresent()){
            return "E-Mail already registered";
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        userRepository.save(user);
        return "User registered successfully";
    }

    public boolean login(String email, String password){
        Optional<User> user = userRepository.findByEmail(email);

        return user.isPresent() && user.get().getPassword().equals(password);
    }
}
