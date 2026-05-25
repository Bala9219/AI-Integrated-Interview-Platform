package com.example.AI_Interview_Platform.controller;

import com.example.AI_Interview_Platform.DTO.AnswerDtos.*;
import com.example.AI_Interview_Platform.DTO.EvaluationDtos.*;
import com.example.AI_Interview_Platform.entity.Answer;
import com.example.AI_Interview_Platform.service.AnswerService;
import com.example.AI_Interview_Platform.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EvaluationController {

    private final AnswerService answerService;
    private final EvaluationService evaluationService;

    @PostMapping("/evaluate-answer")
    public EvaluateAnswersResponse evaluate(@RequestBody EvaluateAnswersRequest request){
        List<EvaluationService.QA> pairs = new ArrayList<>();

        if(request.getAnswers() != null){
            List<Answer> toSave = new ArrayList<>();
            for(SubmitAnswerItem item: request.getAnswers()){
                Answer ans = new Answer();
                ans.setAnswerText(item.getAnswerText());
                ans.setQuestionId(item.getQuestionId());
                toSave.add(ans);
            }
            List<Answer> saved = answerService.saveAll(toSave);

            for(int i=0; i<request.getAnswers().size(); i++){
                SubmitAnswerItem item = request.getAnswers().get(i);
                Long answer_id = saved.get(i).getId();
                pairs.add(new EvaluationService.QA(item.getQuestionText(), item.getAnswerText(), answer_id));
            }
        }
        EvaluationService.OverallResult result = evaluationService.evaluateAnswerPairs(pairs);
        return new EvaluateAnswersResponse(true, result.score,
                result.fillerWords, result.confidence, result.relevance, result.strengths, result.weaknesses,
                result.recommendations);
    }
}
