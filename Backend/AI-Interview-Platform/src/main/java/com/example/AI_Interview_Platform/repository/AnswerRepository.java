package com.example.AI_Interview_Platform.repository;

import com.example.AI_Interview_Platform.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuestionIdIn(List<Long> questionIds);
}
