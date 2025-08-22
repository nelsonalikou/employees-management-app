package com.employees.backend.service;

import com.employees.backend.api.model.Employee;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private List<Employee> employees;

    public EmployeeService() {
        employees = new ArrayList<>();
        // Sample data
        employees.add(new Employee(1, LocalDate.of(1990, 1, 1), "John", "Doe", Employee.Gender.M, LocalDate.of(2020, 1, 1)));
        employees.add(new Employee(2, LocalDate.of(1985, 5, 15), "Jane", "Smith", Employee.Gender.F, LocalDate.of(2019, 3, 10)));
        employees.add(new Employee(3, LocalDate.of(1992, 7, 20), "Alice", "Johnson", Employee.Gender.F, LocalDate.of(2021, 6, 5)));
        employees.add(new Employee(4, LocalDate.of(1988, 11, 30), "Bob", "Brown", Employee.Gender.M, LocalDate.of(2018, 9, 12)));
        employees.add(new Employee(5, LocalDate.of(1995, 3, 25), "Charlie", "Davis", Employee.Gender.M, LocalDate.of(2022, 2, 20)));
    } 
    
    public List<Employee> getAllEmployees() {
        return employees;
    }

    public Optional<Employee> getEmployeeById(Integer id) {
        Optional<Employee> employee = Optional.empty();
        for (Employee emp : employees) {
            if (emp.getEmpNo().equals(id)) {
                employee = Optional.of(emp);
                return employee;
            }
        }
        return employee;
    }
}
