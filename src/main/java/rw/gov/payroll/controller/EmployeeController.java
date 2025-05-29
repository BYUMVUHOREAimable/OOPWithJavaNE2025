package rw.gov.payroll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rw.gov.payroll.dto.ApiResponse;
import rw.gov.payroll.dto.EmployeeDto;
import rw.gov.payroll.service.EmployeeService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<EmployeeDto>>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", employees));
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN') or @securityService.isCurrentUser(#code)")
    public ResponseEntity<ApiResponse<EmployeeDto>> getEmployeeByCode(@PathVariable String code) {
        EmployeeDto employee = employeeService.getEmployeeByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Employee retrieved successfully", employee));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<EmployeeDto>> getCurrentEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        EmployeeDto employee = employeeService.getEmployeeByEmail(email);
        return ResponseEntity.ok(ApiResponse.success("Current employee retrieved successfully", employee));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeDto>> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeDto createdEmployee = employeeService.createEmployee(employeeDto);
        return ResponseEntity.ok(ApiResponse.success("Employee created successfully", createdEmployee));
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN') or @securityService.isCurrentUser(#code)")
    public ResponseEntity<ApiResponse<EmployeeDto>> updateEmployee(
            @PathVariable String code,
            @Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(code, employeeDto);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", updatedEmployee));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable String code) {
        employeeService.deleteEmployee(code);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully"));
    }
}
