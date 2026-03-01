package com.example.demo.repository.h2;

import com.example.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface H2StudentRepo extends JpaRepository<Student, Integer> {
    // This repository is managed by H2Config
}