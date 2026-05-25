package com.example.AI_Interview_Platform.controller;

import com.example.AI_Interview_Platform.entity.Question;
import com.example.AI_Interview_Platform.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/{interviewId}")
    public List<Question> getQuestions(@PathVariable Long interviewId){
        return questionService.getQuestionsForInterview(interviewId);
    }
}
