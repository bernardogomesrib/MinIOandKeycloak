package com.quizz.cal.JPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.cal.objects.Tag;

public interface TagsJPA extends JpaRepository<Tag, Long> {
    
}
