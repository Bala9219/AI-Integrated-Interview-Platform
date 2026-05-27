package com.example.AI_Interview_Platform.repository;

import com.example.AI_Interview_Platform.entity.Interview;
import com.example.AI_Interview_Platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByUser(User user);
}
