package com.example.AI_Interview_Platform.service;

import com.example.AI_Interview_Platform.entity.Evaluation;
import com.example.AI_Interview_Platform.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final AIService aiService;

    public static class QA{
        public final String questionText, answerText;
        public final Long answerId;

        public QA(String q, String a, Long id) {
            this.questionText = q==null ? "" : q;
            this.answerText = a==null ? "" : a;
            this.answerId = id;
        }
    }

    public static class OverallResult{
        public int score, fillerWords, confidence;
        public String relevance = "medium";
        public List<String> strengths = new ArrayList<>();
        public List<String> weaknesses = new ArrayList<>();
        public List<String> recommendations = new ArrayList<>();
    }

    public OverallResult evaluateAnswerPairs(List<QA> pairs){
        OverallResult result = new OverallResult();
        if(pairs==null || pairs.isEmpty()){
            result.weaknesses.add("No answers were provided for the particular questions!");
            result.recommendations.add("Complete interview before evaluating");
            return result;
        }

        List<AIService.AIEvaluation> evaluations = aiService.evaluateBatch(pairs);

        if(evaluations==null || evaluations.isEmpty()){
            result.score = 50;
            result.confidence = 50;

            result.strengths.add("Good Attempt.");
            result.weaknesses.add("AI evaluation failed. Attempt interview again.");
            result.recommendations.add("Try again later.");

            return result;
        }

        int total = 0;
        for(int i=0; i<evaluations.size(); i++){
            AIService.AIEvaluation ev = evaluations.get(i);
            QA qa = pairs.get(i);
            int score = ev.score>0 ? ev.score : 50;
            total += score;

            if(ev.strengths != null){
                result.strengths.addAll(ev.strengths);
            }
            if(ev.weaknesses != null){
                result.weaknesses.addAll(ev.weaknesses);
            }
            if(ev.recommendations != null){
                result.recommendations.addAll(ev.recommendations);
            }
            if(ev.relevance != null){
                if("high".equalsIgnoreCase(ev.relevance)){
                    result.relevance = "high";
                } else if ("low".equalsIgnoreCase(ev.relevance) && !"high".equalsIgnoreCase(result.relevance)) {
                    result.relevance = "low";
                }
            }
            result.fillerWords += ev.fillerWords;

            if(qa.answerId != null){
                evaluationRepository.save(new Evaluation(null, qa.answerId, score, "AI Score " + score));
            }
        }
        result.score = total / pairs.size();
        result.confidence = result.score;
        return result;
    }
}
