package com.quizz.cal.objects;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
@Data
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    
    @ManyToMany
    @NotNull(message = "Quiz questions must not be null")
    @NotEmpty(message = "Quiz must have at least one question")
    private List<Question> questions;
    
    
    @Positive(message = "Quiz must have a positive time limit")
    @NotNull(message = "Quiz must have a time limit")
    private int timeLimit;
    
    
    
    @NotEmpty(message="Quiz must have retry quantity")
    private int retryQuantity;

    
    @ManyToMany
    private List<Tag> tags;



    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

}
