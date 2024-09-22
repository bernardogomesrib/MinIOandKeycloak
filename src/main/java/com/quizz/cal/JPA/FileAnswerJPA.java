package com.quizz.cal.JPA;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.cal.objects.fileObjects.FileAnswer;
public interface FileAnswerJPA extends JpaRepository<FileAnswer,UUID>{
    
}
