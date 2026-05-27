package com.example.AI_Interview_Platform.service;

import com.example.AI_Interview_Platform.DTO.DashboardResponse;
import com.example.AI_Interview_Platform.entity.Interview;
import com.example.AI_Interview_Platform.entity.User;
import com.example.AI_Interview_Platform.repository.EvaluationRepository;
import com.example.AI_Interview_Platform.repository.InterviewRepository;
import com.example.AI_Interview_Platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final InterviewRepository interviewRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public DashboardResponse buildDashboard(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        List<Interview> finished = interviewRepository.findByUser(user).stream().filter(interview -> interview.getFinalScore()!=null).toList();
        if(finished.isEmpty()){
            return mainDashboard();
        }

        int average = (int) Math.round(finished.stream().mapToInt(Interview::getFinalScore).average().orElse(0));
        int best = finished.stream().mapToInt(Interview::getFinalScore).max().orElse(0);
        int totalMinutes = finished.stream().mapToInt(iv->iv.getDuration()==null ? 0 : iv.getDuration()).sum();

        return DashboardResponse.builder()
                .totalInterviews(finished.size())
                .averageScore(average)
                .totalPracticeTime(formatMin(totalMinutes))
                .bestScore(best)
                .scoreTrend(buildingTrend(finished))
                .weakAreas(buildWeakAreas(finished))
                .recentInterviews(buildRecent(finished))
                .strengths(buildStrengths(average))
                .build();
    }

    private String formatMin(int totalMinutes) {
        if(totalMinutes<=0){
            return "0 min";
        }
        int hours = totalMinutes/60, minutes = totalMinutes%60;
        return hours==0 ? minutes+" m" : hours+" h "+minutes+" m";
    }

    private List<DashboardResponse.TrendPoint> buildingTrend(List<Interview> finished) {
        LocalDate today = LocalDate.now();

        Map<LocalDate, List<Integer>> byDay = finished.stream().collect(Collectors.groupingBy(
                iv -> iv.getCompletedAt() == null? today : iv.getCompletedAt().toLocalDate(),
                Collectors.mapping(Interview::getFinalScore, Collectors.toList())
        ));

        return IntStream.rangeClosed(0,6).boxed()
                .sorted(Comparator.reverseOrder())
                .map(offset -> {
                    LocalDate d = today.minusDays(offset);
                    List<Integer> scores = byDay.getOrDefault(d,List.of());
                    int dayAverage = scores.isEmpty()? 0 :
                            (int) scores.stream().mapToInt(Integer::intValue).average().orElse(0);
                    return new DashboardResponse.TrendPoint(d.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) , dayAverage);
                })
                .toList();
    }

    private List<DashboardResponse.NamedValue> buildWeakAreas(List<Interview> finished) {
        Map<String, List<Integer>> byRole = finished.stream().collect(Collectors.groupingBy(
                iv -> iv.getRole() == null ? "General Role" : iv.getRole(),Collectors.mapping(Interview::getFinalScore,Collectors.toList())
        ));

        return byRole.entrySet().stream()
                .map(e -> {
                    int roleAvg = (int) e.getValue().stream().mapToInt(Integer::intValue).average().orElse(0);
                    return new DashboardResponse.NamedValue(e.getKey(), Math.max(5,100 - roleAvg));
                })
                .sorted(Comparator.comparingInt(DashboardResponse.NamedValue::getValue).reversed())
                .limit(4)
                .toList();

    }

    private List<DashboardResponse.NamedValue> buildStrengths(int average) {
        int base = Math.max(40, average);
        return List.of(
                new DashboardResponse.NamedValue("Problem Solving", clamp(base+5)),
                new DashboardResponse.NamedValue("Communication",   clamp(base - 5)),
                new DashboardResponse.NamedValue("Confidence",      clamp(base - 10)),
                new DashboardResponse.NamedValue("Technical Depth", clamp(base))
        );
    }

    private int clamp(int v){
        return Math.max(0, Math.min(100, v));
    }

    private List<DashboardResponse.RecentInterviews> buildRecent(List<Interview> finished) {
        return finished.stream()
                .sorted(Comparator.comparingLong(Interview::getId).reversed())
                .limit(5)
                .map(iv->{
                            int score = iv.getFinalScore();
                            String status = score>=70? "Completed"
                                    :score>=40? "Needs Review"
                                    :"Practiced";
                            LocalDate when = iv.getCompletedAt()!=null
                                    ?iv.getCompletedAt().toLocalDate():LocalDate.now();

                            return DashboardResponse.RecentInterviews.builder()
                                    .role(iv.getRole()==null ?"General Role":iv.getRole())
                                    .level(iv.getDifficultyLevel()==null? "Medium": iv.getDifficultyLevel())
                                    .date(when.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                                    .score(score)
                                    .status(status)
                                    .build();
                }).toList();
    }

    private DashboardResponse mainDashboard(){

        return DashboardResponse.builder()
                .totalInterviews(0L)
                .averageScore(0)
                .bestScore(0)
                .totalPracticeTime("0h 0m")
                .scoreTrend(List.of())
                .weakAreas(List.of())
                .recentInterviews(List.of())
                .strengths(List.of())
                .build();
    }

    private DashboardResponse.RecentInterviews demoRow(String role, String level, LocalDate date, int score, String status) {
        return DashboardResponse.RecentInterviews.builder()
                .role(role).level(level)
                .date(date.format(DATE_FMT))
                .score(score).status(status)
                .build();
    }
}
