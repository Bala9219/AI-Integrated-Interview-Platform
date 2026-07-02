package com.example.AI_Interview_Platform.service;

import com.example.AI_Interview_Platform.DTO.InterviewReportDto;
import com.example.AI_Interview_Platform.entity.*;
import com.example.AI_Interview_Platform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final InterviewRepository interviewRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final EvaluationRepository evaluationRepository;
    private final ReportRepository reportRepository;

    public InterviewReportDto getReport(Long interviewId){

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        List<Question> questions = questionRepository.findByInterviewId(interviewId);

        List<Long> questionIds = questions.stream().map(Question::getId).collect(Collectors.toList());

        List<Answer> answers = questionIds.isEmpty() ? List.of() : answerRepository.findByQuestionIdIn(questionIds);

        List<Long> answerIds = answers.stream().map(Answer::getId).collect(Collectors.toList());

        List<Evaluation> evaluations = answerIds.isEmpty() ? List.of() : evaluationRepository.findByAnswerIdIn(answerIds);

        int averageScore = 0;

        if(!evaluations.isEmpty()){
            averageScore = (int) evaluations.stream()
                    .mapToInt(Evaluation::getScore)
                    .average()
                    .orElse(0);
        }

        InterviewReportDto dto = new InterviewReportDto();

        dto.setInterviewId(interview.getId());
        dto.setRole(interview.getRole());
        dto.setExperienceLevel(interview.getExperienceLevel());
        dto.setDifficultyLevel(interview.getDifficultyLevel());

        dto.setScore(interview.getFinalScore() != null ? interview.getFinalScore() : averageScore);

        dto.setCompletedAt(interview.getCompletedAt()==null
                        ? ""
                        : interview.getCompletedAt().toString());

        Report report = reportRepository.findByInterviewId(interviewId).orElse(null);

        if(report != null){

            dto.setConfidence(report.getConfidence());
            dto.setFillerWords(report.getFillerWords());
            dto.setRelevance(report.getRelevance());

            dto.setStrengths(
                    report.getStrengths()==null || report.getStrengths().isBlank()
                            ? List.of()
                            : List.of(report.getStrengths().split("\\|\\|"))
            );

            dto.setWeaknesses(
                    report.getWeaknesses()==null || report.getWeaknesses().isBlank()
                            ? List.of()
                            : List.of(report.getWeaknesses().split("\\|\\|"))
            );

            dto.setRecommendations(
                    report.getRecommendations()==null || report.getRecommendations().isBlank()
                            ? List.of()
                            : List.of(report.getRecommendations().split("\\|\\|"))
            );
        }
        else{
            dto.setStrengths(List.of());
            dto.setWeaknesses(List.of());
            dto.setRecommendations(List.of());
        }

        return dto;
    }

    public void saveReport(Long interviewId, EvaluationService.OverallResult result){
        Report report=new Report();

        report.setInterviewId(interviewId);
        report.setScore(result.score);
        report.setConfidence(result.confidence);
        report.setFillerWords(result.fillerWords);
        report.setRelevance(result.relevance);

        report.setStrengths(String.join("||",result.strengths));
        report.setWeaknesses(String.join("||",result.weaknesses));
        report.setRecommendations(String.join("||",result.recommendations));

        reportRepository.save(report);
    }
}
