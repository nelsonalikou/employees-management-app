package com.employees.backend;

import com.employees.backend.api.controller.EmployeeController;
import com.employees.backend.api.model.Employee;
import com.employees.backend.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    // Test Case 1: Get all employees - populated list
    @Test
    void getAllEmployees_shouldReturnAllEmployees() throws Exception {
        Employee employee1 = new Employee(1, LocalDate.of(1990, 1, 1), "John", "Doe", Employee.Gender.M, LocalDate.of(2020, 1, 1));
        Employee employee2 = new Employee(2, LocalDate.of(1985, 5, 15), "Jane", "Smith", Employee.Gender.F, LocalDate.of(2019, 3, 10));
        List<Employee> allEmployees = Arrays.asList(employee1, employee2);

        when(employeeService.getAllEmployees()).thenReturn(allEmployees);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(allEmployees.size()))
                .andExpect(jsonPath("$[0].empNo").value(employee1.getEmpNo()))
                .andExpect(jsonPath("$[1].empNo").value(employee2.getEmpNo()));
    }

    // Test Case 2: Get all employees - empty list
    @Test
    void getAllEmployees_shouldReturnEmptyList_whenNoEmployeesExist() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    // Test Case 3: Get employee by ID - valid ID
    @Test
    void getEmployeeById_shouldReturnEmployee_whenIdIsValid() throws Exception {
        Employee employee = new Employee(1, LocalDate.of(1990, 1, 1), "John", "Doe", Employee.Gender.M, LocalDate.of(2020, 1, 1));
        when(employeeService.getEmployeeById(1)).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empNo").value(employee.getEmpNo()))
                .andExpect(jsonPath("$.firstName").value(employee.getFirstName()));
    }

    // Test Case 4: Get employee by ID - invalid ID
    @Test
    void getEmployeeById_shouldReturnNotFound_whenIdIsInvalid() throws Exception {
        when(employeeService.getEmployeeById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/employees/99"))
                .andExpect(status().isNotFound());
    }

    // Test Case 5: Get employee by ID - non-integer ID
    @Test
    void getEmployeeById_shouldReturnBadRequest_whenIdIsNonInteger() throws Exception {
        mockMvc.perform(get("/employees/abc"))
                .andExpect(status().isBadRequest());
    }
}