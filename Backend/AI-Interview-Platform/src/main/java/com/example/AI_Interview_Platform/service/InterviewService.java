package com.example.AI_Interview_Platform.service;

import com.example.AI_Interview_Platform.entity.Interview;
import com.example.AI_Interview_Platform.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;

    public Interview create(String role, String experienceLevel, String difficultyLevel, Integer duration){
        Interview interview = new Interview();

        interview.setRole(role);
        interview.setExperienceLevel(experienceLevel);
        interview.setDifficultyLevel(difficultyLevel);
        interview.setDuration(duration);

        return interviewRepository.save(interview);
    }

    public Interview finish(Long id, Integer finalScore){
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found for Id: " +id));

        interview.setFinalScore(finalScore);
        interview.setCompletedAt(LocalDateTime.now());
        return interviewRepository.save(interview);
    }
}
