package com.example.AI_Interview_Platform.service;

import com.example.AI_Interview_Platform.entity.Answer;
import com.example.AI_Interview_Platform.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer save(Long questionId, String answerText){
        Answer ans = new Answer();

        ans.setQuestionId(questionId);
        ans.setAnswerText(answerText);

        return answerRepository.save(ans);
    }

    public List<Answer> saveAll(List<Answer> answers){
        return answerRepository.saveAll(answers);
    }
}
