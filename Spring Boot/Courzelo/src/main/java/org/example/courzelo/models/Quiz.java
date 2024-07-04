package org.example.courzelo.models;

import lombok.Data;
import org.example.courzelo.dto.QuestionDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "quizzes")
@Data
public class Quiz {
    @Id
    private String id;
    private String title;
    private String description;
    private List<Question> questions;
    private boolean isSelected;
    private double score;
    private String Status;
    private int duration; // in minutes
    private int maxAttempts;
    private String category;


    /*   id?: string;
    title: string;
    description: string;
    questions: Question[] = [];
    isSelected: boolean;
    score: number;
    Status: string;
    duration: number;
    maxAttempts: number;
    category: string;;*/
}
