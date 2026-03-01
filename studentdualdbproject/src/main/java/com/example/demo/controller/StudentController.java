package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.repository.h2.H2StudentRepo;
import com.example.demo.repository.pg.PGStudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class StudentController {

    @Autowired
    private H2StudentRepo h2Repo;

    @Autowired
    private PGStudentRepo pgRepo;

    @PostMapping("/insertStudent")
    public String insertStudent(@ModelAttribute Student student) {
        // This goes to H2
        h2Repo.save(student);
        
        // This goes to PostgreSQL
        pgRepo.save(student);
        
        // Redirect back to the form or a success page
        return "redirect:/index.html";
    }
}