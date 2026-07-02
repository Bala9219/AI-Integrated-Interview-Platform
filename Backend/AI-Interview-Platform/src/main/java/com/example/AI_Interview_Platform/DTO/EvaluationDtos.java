package com.example.AI_Interview_Platform.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class EvaluationDtos {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EvaluateAnswersRequest{
        private Long interviewId;
        private List<AnswerDtos.SubmitAnswerItem> answers;
    }

    @Data
    @AllArgsConstructor
    public static class EvaluateAnswersResponse{
        private boolean success;
        private int score;
        private int fillerWords;
        private int confidence;
        private String relevance;
        private List<String> strengths;
        private List<String> weaknesses;
        private List<String> recommendations;
    }
}
