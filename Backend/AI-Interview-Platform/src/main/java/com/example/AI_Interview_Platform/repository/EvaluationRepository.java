package com.example.AI_Interview_Platform.repository;

import com.example.AI_Interview_Platform.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    List<Evaluation> findByAnswerIdIn(List<Long> answerIds);
}
