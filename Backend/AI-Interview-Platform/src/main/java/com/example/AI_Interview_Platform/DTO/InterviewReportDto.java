package com.example.AI_Interview_Platform.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewReportDto {

    private Long interviewId;
    private String role;
    private String experienceLevel;
    private String difficultyLevel;
    private Integer score;
    private String completedAt;
    private Integer fillerWords;
    private Integer confidence;
    private String relevance;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> recommendations;

}
