package com.example.demo.repository;

import com.example.demo.model.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
	List<Student> findBySchool(String school);
	long countBySchool(String school);
	long countByStandard(String standard);
	
	List<Student> findByPercentageGreaterThanEqualOrderByPercentageDesc(double percentage);
    List<Student> findByPercentageLessThanOrderByPercentageDesc(double percentage);
    
    long countByGenderAndStandard(String gender, String standard);
}