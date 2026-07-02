package com.example.AI_Interview_Platform.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class InterviewDtos {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InterviewRequest {
        private String role;
        private String experienceLevel;
        private String difficultyLevel;
        private Integer duration;
        private String email;
    }

    @Data
    @AllArgsConstructor
    public static class InterviewResponse{
        private boolean success;
        private String message;
        private Long interviewId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FinishInterviewRequest{
        private Integer finalScore;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyInterviewItem{
        private Long interviewId;
        private String role;
        private String experienceLevel;
        private String difficultyLevel;
        private Integer duration;
        private Integer finalScore;
        private String completedAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyInterviewResponse{
        private boolean success;
        private List<MyInterviewItem> interviews;
    }
}
