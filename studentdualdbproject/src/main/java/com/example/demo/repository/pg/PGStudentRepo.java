package com.example.demo.repository.pg;

import com.example.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PGStudentRepo extends JpaRepository<Student, Integer> {
    // This repository is managed by PostgresConfig
}