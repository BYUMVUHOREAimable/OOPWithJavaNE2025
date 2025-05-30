package rw.gov.payroll.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import rw.gov.payroll.dto.EmployeeDto;
import rw.gov.payroll.model.Employee;
import rw.gov.payroll.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the EmployeeService class.
 * These tests verify the business logic in the service layer.
 */
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    /**
     * Test for getting an employee by code.
     * This test verifies that the service correctly returns an employee when found.
     */
    @Test
    public void testGetEmployeeByCode_WhenEmployeeExists() {
        // Arrange
        String employeeCode = "EMP-123";
        Employee mockEmployee = new Employee();
        mockEmployee.setCode(employeeCode);
        mockEmployee.setFirstName("John");
        mockEmployee.setLastName("Doe");

        when(employeeRepository.findById(employeeCode)).thenReturn(Optional.of(mockEmployee));

        // Act
        EmployeeDto result = employeeService.getEmployeeByCode(employeeCode);

        // Assert
        assertNotNull(result);
        assertEquals(employeeCode, result.getCode());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(employeeRepository, times(1)).findById(employeeCode);
    }

    /**
     * Test for getting an employee by code when the employee doesn't exist.
     * This test verifies that the service throws EntityNotFoundException.
     */
    @Test
    public void testGetEmployeeByCode_WhenEmployeeDoesNotExist() {
        // Arrange
        String employeeCode = "EMP-NONEXISTENT";

        when(employeeRepository.findById(employeeCode)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            employeeService.getEmployeeByCode(employeeCode);
        });

        verify(employeeRepository, times(1)).findById(employeeCode);
    }

    /**
     * Test for getting all employees.
     * This test verifies that the service correctly returns all employees.
     */
    @Test
    public void testGetAllEmployees() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setCode("EMP-1");
        employee1.setFirstName("John");

        Employee employee2 = new Employee();
        employee2.setCode("EMP-2");
        employee2.setFirstName("Jane");

        List<Employee> mockEmployees = Arrays.asList(employee1, employee2);

        when(employeeRepository.findAll()).thenReturn(mockEmployees);

        // Act
        List<EmployeeDto> result = employeeService.getAllEmployees();

        // Assert
        assertEquals(2, result.size());
        assertEquals("EMP-1", result.get(0).getCode());
        assertEquals("EMP-2", result.get(1).getCode());

        verify(employeeRepository, times(1)).findAll();
    }
}
