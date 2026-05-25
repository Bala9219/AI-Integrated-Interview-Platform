package com.example.AI_Interview_Platform.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResponse {

    private long totalInterviews;
    private double averageScore;
    private int bestScore;
    private String totalPracticeTime;

    private List<TrendPoint> scoreTrend;
    private List<NamedValue> weakAreas;
    private List<RecentInterviews> recentInterviews;
    private List<NamedValue> strengths;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrendPoint {
        private String date;
        private int score;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NamedValue{
        private String name;
        private int value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RecentInterviews{
        private String role;
        private String level;
        private String date;
        private int score;
        private String status;
    }
}
