package com.example.AI_Interview_Platform.controller;

import com.example.AI_Interview_Platform.DTO.InterviewReportDto;
import com.example.AI_Interview_Platform.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interview")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/report/{interviewId}")
    public InterviewReportDto getReport(@PathVariable Long interviewId){
        return reportService.getReport(interviewId);
    }
}
