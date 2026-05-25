package com.example.AI_Interview_Platform.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class AnswerDtos {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnswerRequest{
        private Long questionId;
        private String answerText;
    }

    @Data
    @AllArgsConstructor
    public static class AnswerResponse{
        private boolean success;
        private String message;
        private Long answerId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubmitAnswerItem{
        private Long questionId;
        private String questionText;
        private String answerText;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubmitAnswersRequest{
        private List<SubmitAnswerItem> answers;
    }

    @Data
    @AllArgsConstructor
    public static class SubmitAnswersResponse{
        private boolean success;
        private int saved;
    }
}
