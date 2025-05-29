package rw.gov.payroll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.gov.payroll.dto.ApiResponse;
import rw.gov.payroll.dto.EmploymentDto;
import rw.gov.payroll.service.EmploymentService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/employments")
public class EmploymentController {

    @Autowired
    private EmploymentService employmentService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<EmploymentDto>>> getAllEmployments() {
        List<EmploymentDto> employments = employmentService.getAllEmployments();
        return ResponseEntity.ok(ApiResponse.success("Employments retrieved successfully", employments));
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<EmploymentDto>>> getActiveEmployments() {
        List<EmploymentDto> employments = employmentService.getActiveEmployments();
        return ResponseEntity.ok(ApiResponse.success("Active employments retrieved successfully", employments));
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<EmploymentDto>> getEmploymentByCode(@PathVariable String code) {
        EmploymentDto employment = employmentService.getEmploymentByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Employment retrieved successfully", employment));
    }

    @GetMapping("/employee/{employeeCode}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN') or @securityService.isCurrentUser(#employeeCode)")
    public ResponseEntity<ApiResponse<List<EmploymentDto>>> getEmploymentsByEmployee(@PathVariable String employeeCode) {
        List<EmploymentDto> employments = employmentService.getEmploymentsByEmployee(employeeCode);
        return ResponseEntity.ok(ApiResponse.success("Employments for employee retrieved successfully", employments));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<EmploymentDto>> createEmployment(@Valid @RequestBody EmploymentDto employmentDto) {
        EmploymentDto createdEmployment = employmentService.createEmployment(employmentDto);
        return ResponseEntity.ok(ApiResponse.success("Employment created successfully", createdEmployment));
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<EmploymentDto>> updateEmployment(
            @PathVariable String code,
            @Valid @RequestBody EmploymentDto employmentDto) {
        EmploymentDto updatedEmployment = employmentService.updateEmployment(code, employmentDto);
        return ResponseEntity.ok(ApiResponse.success("Employment updated successfully", updatedEmployment));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEmployment(@PathVariable String code) {
        employmentService.deleteEmployment(code);
        return ResponseEntity.ok(ApiResponse.success("Employment deleted successfully"));
    }
}
