package com.example.AI_Interview_Platform.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDtos {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserRequest{
        private String email;
        private String name;
        private String newEmail;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponse{
        private Long id;
        private String name;
        private String email;
    }
}
