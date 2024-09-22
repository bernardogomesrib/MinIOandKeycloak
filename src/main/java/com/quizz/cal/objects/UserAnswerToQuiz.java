package com.quizz.cal.objects;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.quizz.cal.objects.fileObjects.FileAnswer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
@Data
@Entity
public class UserAnswerToQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private Quiz quiz;

    @ManyToOne
    private User user;
    
    private int score;
    
    @ManyToMany
    private List<FileAnswer> fileAnswers;
    @ManyToMany
    private List<TextAnswer> userAnswers;
    
    @ManyToMany
    private List<TextAnswer> mistakes;
    
    @ManyToMany
    private List<FileAnswer> fileMistakes;
    
    @OneToMany
    private List<OpenQuestionAnswer> openQuestionAnswers;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;




}
