package com.example.AI_Interview_Platform.repository;

import com.example.AI_Interview_Platform.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByInterviewId(Long interviewId);
}
