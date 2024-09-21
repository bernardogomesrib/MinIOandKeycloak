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
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToMany
    @NotNull(message = "Question must have at least one answer")
    private List<TextAnswer> answers;
    @NotNull(message = "Question must have a question")
    private String question;
    

    @ManyToMany
    private List<FileAnswer> fileAnswers;
    
    @ManyToMany
    private List<QuestionText> questionTexts;
    @ManyToMany
    @NotNull(message = "Question must have at least one tag")
    private List<Tag> tags;
    @ManyToMany
    private List<OpenQuestionAnswer> openQuestionAnswers;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}