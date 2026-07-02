package com.example.AI_Interview_Platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reports")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long interviewId;

    private Integer score;

    private Integer fillerWords;

    private Integer confidence;

    private String relevance;

    @Column(length = 5000)
    private String strengths;

    @Column(length = 5000)
    private String weaknesses;

    @Column(length = 5000)
    private String recommendations;

}
