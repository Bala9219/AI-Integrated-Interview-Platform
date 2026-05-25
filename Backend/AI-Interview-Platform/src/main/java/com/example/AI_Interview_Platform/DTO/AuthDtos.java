package com.example.AI_Interview_Platform.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDtos {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterRequest{
        private String name;
        private String email;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRequest{
        private String email;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class ApiResponse{
        private boolean success;
        private String message;
    }
}
