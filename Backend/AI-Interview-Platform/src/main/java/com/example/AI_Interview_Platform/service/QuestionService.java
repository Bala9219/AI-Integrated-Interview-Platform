package com.example.AI_Interview_Platform.service;
import com.example.AI_Interview_Platform.entity.Interview;
import com.example.AI_Interview_Platform.entity.Question;
import com.example.AI_Interview_Platform.repository.InterviewRepository;
import com.example.AI_Interview_Platform.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private static final int DEFAULT_QUESTION_COUNT = 5;

    // Generic, fall-back questions if AI fails...
    private static final List<String> FALLBACK_QUESTIONS = List.of(
            "Tell me about yourself and your background.",
            "Describe a challenging project you worked on recently.",
            "What are your biggest strengths and weaknesses?",
            "Why are you interested in this particular role?",
            "Where do you see yourself in 5 years?"
    );

    private final QuestionRepository questionRepository;
    private final InterviewRepository interviewRepository;
    private final com.example.AI_Interview_Platform.service.AIService aiService;

    public List<Question>  getQuestionsForInterview(Long interviewId){
        //  1. Already generated? return cached rows.
        List<Question> existing = questionRepository.findByInterviewId(interviewId);
        if(!existing.isEmpty()){
            return existing;
        }

        // 2. Need the interview to know role/experience/difficulty.
        Optional<Interview> opt = interviewRepository.findById(interviewId);
        if(opt.isEmpty()) return List.of();
        Interview interview = opt.get();

        List<String> texts = aiService.generateQuestions(
                interview.getRole(),
                interview.getExperienceLevel(),
                interview.getDifficultyLevel(),
                DEFAULT_QUESTION_COUNT
        );
        if(texts==null || texts.isEmpty()){
            System.out.println("[QuestionService] AI unavailable - using fallback questions.");
            texts = FALLBACK_QUESTIONS;
        }

        List<Question> toSave = new ArrayList<>();
        for(String text: texts){
            toSave.add(new Question(null, interviewId, text));
        }
        return questionRepository.saveAll(toSave);
    }
}
