package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Employee;

@Service
public class EmployeeService {

    private Map<Integer, Employee> employeeMap = new HashMap<>();

    public EmployeeService() {
        // Dummy Data (No DB)
        employeeMap.put(1, new Employee(1, "John"));
        employeeMap.put(2, new Employee(2, "Alice"));
    }

    public Optional<Employee> getEmployees(int id) {
        return Optional.ofNullable(employeeMap.get(id));
    }
}