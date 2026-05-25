package com.example.AI_Interview_Platform.repository;

import com.example.AI_Interview_Platform.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByInterviewId(Long interviewId);
}
