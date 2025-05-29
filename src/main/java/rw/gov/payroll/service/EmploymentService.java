package rw.gov.payroll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rw.gov.payroll.dto.EmploymentDto;
import rw.gov.payroll.model.Employee;
import rw.gov.payroll.model.Employment;
import rw.gov.payroll.model.Employment.EmploymentStatus;
import rw.gov.payroll.repository.EmployeeRepository;
import rw.gov.payroll.repository.EmploymentRepository;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmploymentService {

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmploymentDto> getAllEmployments() {
        return employmentRepository.findAll().stream()
                .map(EmploymentDto::new)
                .collect(Collectors.toList());
    }

    public List<EmploymentDto> getEmploymentsByEmployee(String employeeCode) {
        Employee employee = employeeRepository.findById(employeeCode)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with code: " + employeeCode));

        return employmentRepository.findByEmployee(employee).stream()
                .map(EmploymentDto::new)
                .collect(Collectors.toList());
    }

    public List<EmploymentDto> getActiveEmployments() {
        return employmentRepository.findByStatus(EmploymentStatus.ACTIVE).stream()
                .map(EmploymentDto::new)
                .collect(Collectors.toList());
    }

    public EmploymentDto getEmploymentByCode(String code) {
        Employment employment = employmentRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Employment not found with code: " + code));
        return new EmploymentDto(employment);
    }

    public EmploymentDto createEmployment(EmploymentDto employmentDto) {
        Employee employee = employeeRepository.findById(employmentDto.getEmployeeCode())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with code: " + employmentDto.getEmployeeCode()));

        Employment employment = new Employment();
        employment.setCode(generateEmploymentCode());
        employment.setEmployee(employee);
        employment.setDepartment(employmentDto.getDepartment());
        employment.setPosition(employmentDto.getPosition());
        employment.setBaseSalary(employmentDto.getBaseSalary());
        employment.setStatus(employmentDto.getStatus() != null ? employmentDto.getStatus() : EmploymentStatus.ACTIVE);
        employment.setJoiningDate(employmentDto.getJoiningDate() != null ? employmentDto.getJoiningDate() : LocalDate.now());

        Employment savedEmployment = employmentRepository.save(employment);
        return new EmploymentDto(savedEmployment);
    }

    public EmploymentDto updateEmployment(String code, EmploymentDto employmentDto) {
        Employment existingEmployment = employmentRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Employment not found with code: " + code));

        // Update fields
        if (employmentDto.getDepartment() != null) {
            existingEmployment.setDepartment(employmentDto.getDepartment());
        }

        if (employmentDto.getPosition() != null) {
            existingEmployment.setPosition(employmentDto.getPosition());
        }

        if (employmentDto.getBaseSalary() != null) {
            existingEmployment.setBaseSalary(employmentDto.getBaseSalary());
        }

        if (employmentDto.getStatus() != null) {
            existingEmployment.setStatus(employmentDto.getStatus());
        }

        if (employmentDto.getJoiningDate() != null) {
            existingEmployment.setJoiningDate(employmentDto.getJoiningDate());
        }

        // If employee is being changed, verify the new employee exists
        if (employmentDto.getEmployeeCode() != null && 
                !employmentDto.getEmployeeCode().equals(existingEmployment.getEmployee().getCode())) {
            Employee newEmployee = employeeRepository.findById(employmentDto.getEmployeeCode())
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with code: " + employmentDto.getEmployeeCode()));
            existingEmployment.setEmployee(newEmployee);
        }

        Employment updatedEmployment = employmentRepository.save(existingEmployment);
        return new EmploymentDto(updatedEmployment);
    }

    public void deleteEmployment(String code) {
        if (!employmentRepository.existsById(code)) {
            throw new EntityNotFoundException("Employment not found with code: " + code);
        }
        employmentRepository.deleteById(code);
    }

    private String generateEmploymentCode() {
        return "EMPL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
