package com.employees.backend.api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.employees.backend.api.model.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;
import org.springframework.web.server.ResponseStatusException;
import com.employees.backend.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Integer id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            return employee.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
    }
}
