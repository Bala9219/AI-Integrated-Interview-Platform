package com.example.AI_Interview_Platform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "evaluations")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long answerId;

    private Integer score;

    @Column(length = 10000)
    private String feedback;
}
