package com.example.AI_Interview_Platform.controller;

import com.example.AI_Interview_Platform.DTO.AnswerDtos.*;
import com.example.AI_Interview_Platform.entity.Answer;
import com.example.AI_Interview_Platform.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public SubmitAnswersResponse submitAnswers(@RequestBody SubmitAnswersRequest request){
        List<Answer> toSave = new ArrayList<>();

        if(request.getAnswers() != null){
            for(SubmitAnswerItem item: request.getAnswers()){
                Answer ans = new Answer();
                ans.setQuestionId(item.getQuestionId());
                ans.setAnswerText(item.getAnswerText());
                toSave.add(ans);
            }
        }
        List<Answer> saved = answerService.saveAll(toSave);
        return new SubmitAnswersResponse(true, saved.size());
    }
}
