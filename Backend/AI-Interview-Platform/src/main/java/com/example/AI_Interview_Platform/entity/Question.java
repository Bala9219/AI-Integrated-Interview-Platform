package com.example.AI_Interview_Platform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questions")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long interviewId;

    @Column(length = 5000)
    private String questionText;
}
