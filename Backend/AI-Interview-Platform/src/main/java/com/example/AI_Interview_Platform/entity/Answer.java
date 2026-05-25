package com.example.AI_Interview_Platform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answers")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questionId;

    @Column(length = 5000)
    private String answerText;
}
