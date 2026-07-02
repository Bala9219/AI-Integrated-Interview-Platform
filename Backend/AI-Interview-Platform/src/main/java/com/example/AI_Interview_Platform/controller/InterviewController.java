package com.example.AI_Interview_Platform.controller;

import com.example.AI_Interview_Platform.DTO.InterviewDtos.*;
import com.example.AI_Interview_Platform.entity.Interview;
import com.example.AI_Interview_Platform.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interview")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/create")
    public InterviewResponse create(@RequestBody InterviewRequest request){
        Interview saved = interviewService.create(request.getRole(), request.getExperienceLevel(), request.getDifficultyLevel(), request.getDuration(), request.getEmail());

        return new InterviewResponse(true, "Interview Created", saved.getId());
    }

    @PostMapping("/{id}/finish")
    public InterviewResponse finish(@PathVariable Long id, @RequestBody FinishInterviewRequest request){
        Interview saved = interviewService.finish(id, request.getFinalScore());

        return new InterviewResponse(true, "Interview Finished", saved.getId());
    }

    @GetMapping("/my/{email}")
    public MyInterviewResponse getMyInterviews(@PathVariable String email){
        List<MyInterviewItem> interviews = interviewService.getUserInterviews(email);

        return new MyInterviewResponse(true, interviews);
    }
}
