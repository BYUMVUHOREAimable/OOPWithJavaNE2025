package rw.gov.payroll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rw.gov.payroll.dto.EmployeeDto;
import rw.gov.payroll.model.Employee;
import rw.gov.payroll.repository.EmployeeRepository;
import rw.gov.payroll.security.SecurityConstants;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EmployeeDto::new)
                .collect(Collectors.toList());
    }

    public EmployeeDto getEmployeeByCode(String code) {
        Employee employee = employeeRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with code: " + code));
        return new EmployeeDto(employee);
    }

    public EmployeeDto getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));
        return new EmployeeDto(employee);
    }

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        if (employeeRepository.existsByEmail(employeeDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        Employee employee = employeeDto.toEntity();

        // Generate employee code if not provided
        if (employee.getCode() == null || employee.getCode().isEmpty()) {
            employee.setCode(generateEmployeeCode());
        }

        // Set default status if not provided
        if (employee.getStatus() == null) {
            employee.setStatus(Employee.EmployeeStatus.ACTIVE);
        }

        // Set default role if not provided
        if (employee.getRoles() == null || employee.getRoles().isEmpty()) {
            Set<String> roles = new HashSet<>();
            roles.add(SecurityConstants.ROLE_EMPLOYEE);
            employee.setRoles(roles);
        }

        // Encode password if provided
        if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return new EmployeeDto(savedEmployee);
    }

    public EmployeeDto updateEmployee(String code, EmployeeDto employeeDto) {
        Employee existingEmployee = employeeRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with code: " + code));

        // Check if email is being changed and if it's already in use
        if (!existingEmployee.getEmail().equals(employeeDto.getEmail()) && 
                employeeRepository.existsByEmail(employeeDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Update fields
        existingEmployee.setFirstName(employeeDto.getFirstName());
        existingEmployee.setLastName(employeeDto.getLastName());
        existingEmployee.setEmail(employeeDto.getEmail());
        existingEmployee.setMobile(employeeDto.getMobile());
        existingEmployee.setDateOfBirth(employeeDto.getDateOfBirth());

        if (employeeDto.getStatus() != null) {
            existingEmployee.setStatus(employeeDto.getStatus());
        }

        if (employeeDto.getRoles() != null && !employeeDto.getRoles().isEmpty()) {
            existingEmployee.setRoles(employeeDto.getRoles());
        }

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return new EmployeeDto(updatedEmployee);
    }

    public void deleteEmployee(String code) {
        if (!employeeRepository.existsById(code)) {
            throw new EntityNotFoundException("Employee not found with code: " + code);
        }
        employeeRepository.deleteById(code);
    }

    private String generateEmployeeCode() {
        return "EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
