package com.quizz.cal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quizz.cal.JPA.QuestionJPA;
import com.quizz.cal.objects.Question;




@RestController()
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionJPA questionJPA;

    @PostMapping()
    public Question post(@RequestBody Question entity) {
        questionJPA.save(entity);
        return entity;
    }
    @GetMapping("{id}")
    public Question get(@RequestParam long id) {
        var r = questionJPA.findById(id);
        if(r.isPresent()) {
            return r.get();
        }else{
            return null;
        }
    }
    @PutMapping()
    public Question putMethodName(@RequestBody Question entity) {
        var r = questionJPA.findById(entity.getId());
        if(r.isPresent()) {
            Question q = questionJPA.save(entity);
            return q;
        }else{
            return null;
        }
    }
    @DeleteMapping({"{id}"})
    public boolean deleteMethodName(@RequestParam long id) {
        var r = questionJPA.findById(id);
        if(r.isPresent()) {
            questionJPA.delete(r.get());
            return true;
        }else{
            return false;
        }
        
    }
}
