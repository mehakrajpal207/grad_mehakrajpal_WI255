package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository repository;

    // GET all students
    @GetMapping
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    // GET specific student by regNo
    @GetMapping("/{regNo}")
    public ResponseEntity<Student> getStudentById(@PathVariable String regNo) {
        return repository.findById(regNo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Insert record
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return repository.save(student);
    }

    // PUT - Update full record
    @PutMapping("/{regNo}")
    public ResponseEntity<Student> updateStudent(@PathVariable String regNo, @RequestBody Student details) {
        return repository.findById(regNo).map(student -> {
            student.setRollNo(details.getRollNo());
            student.setName(details.getName());
            student.setStandard(details.getStandard());
            student.setSchool(details.getSchool());
            student.setGender(details.getGender());
            student.setPercentage(details.getPercentage());
            return ResponseEntity.ok(repository.save(student));
        }).orElse(ResponseEntity.notFound().build());
    }

    // PATCH - Update specific attributes
    @PatchMapping("/{regNo}")
    public ResponseEntity<Student> patchStudent(@PathVariable String regNo, @RequestBody Map<String, Object> updates) {
        return repository.findById(regNo).map(student -> {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "rollNo" -> student.setRollNo(Integer.parseInt(value.toString()));
                    case "name" -> student.setName((String) value);
                    case "standard" -> student.setStandard((String) value);
                    case "school" -> student.setSchool((String) value);
                    case "gender" -> student.setGender((String) value);
                    case "percentage" -> student.setPercentage(Double.parseDouble(value.toString()));
                 }
            });
            return ResponseEntity.ok(repository.save(student));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE record
    @DeleteMapping("/{regNo}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String regNo) {
        if (repository.existsById(regNo)) {
            repository.deleteById(regNo);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    //Student by School name
    @GetMapping("/school")
    public List<Student> getStudentsBySchool(@RequestParam String name) {
        return repository.findBySchool(name);
    }
    
    // in that school
    @GetMapping("/school/count")
    public long getSchoolCount(@RequestParam String name) {
        return repository.countBySchool(name);
    }
    
    //Total number of students in a specific standard
    @GetMapping("/school/standard/count")
    public long getStandardCount(@RequestParam("class") String standard) {
        return repository.countByStandard(standard);
    }

    // List students by Pass/Fail in descending order
    @GetMapping("/result")
    public List<Student> getStudentsByResult(@RequestParam boolean pass) {
        if (pass) {
            return repository.findByPercentageGreaterThanEqualOrderByPercentageDesc(40.0);
        } else {
            return repository.findByPercentageLessThanOrderByPercentageDesc(40.0);
        }
    }

    //Strength by Gender and Standard
    @GetMapping("/strength")
    public long getStrengthByGenderAndStandard(
            @RequestParam String gender, 
            @RequestParam String standard) {
        return repository.countByGenderAndStandard(gender, standard);
    }
    
}