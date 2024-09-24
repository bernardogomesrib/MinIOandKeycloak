package com.quizz.cal.objects;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.quizz.cal.objects.fileObjects.FileAnswer;
import com.quizz.cal.objects.fileObjects.FileOnTheQuestion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
@Data
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(columnDefinition = "boolean default false")
    private boolean isDone;
    
    @NotNull(message = "Question must have a question")
    private String question;
    
    @Positive(message = "Question must have a positive order")
    private int sequencialOrder;
    
    @ManyToMany
    @NotNull(message = "Question must have at least one tag")
    private List<Tag> tags;
    
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Quiz> quizzes;
    
    @OneToMany
    private List<OpenQuestionAnswer> openQuestionAnswers;
    @OneToMany
    private List<FileAnswer> fileAnswers;
    @OneToMany
    private List<TextAnswer> answers;
    @OneToMany
    private List<FileOnTheQuestion> files;
    @OneToMany
    private List<QuestionText> questionTexts;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @Column(columnDefinition = "boolean default false")
    private boolean isPublic;

    @ManyToOne
    private User author;
}