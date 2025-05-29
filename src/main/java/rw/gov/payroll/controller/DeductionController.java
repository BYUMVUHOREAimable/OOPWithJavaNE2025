package rw.gov.payroll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.gov.payroll.dto.ApiResponse;
import rw.gov.payroll.dto.DeductionDto;
import rw.gov.payroll.service.DeductionService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/deductions")
public class DeductionController {

    @Autowired
    private DeductionService deductionService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<ApiResponse<List<DeductionDto>>> getAllDeductions() {
        List<DeductionDto> deductions = deductionService.getAllDeductions();
        return ResponseEntity.ok(ApiResponse.success("Deductions retrieved successfully", deductions));
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<ApiResponse<DeductionDto>> getDeductionByCode(@PathVariable String code) {
        DeductionDto deduction = deductionService.getDeductionByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Deduction retrieved successfully", deduction));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<ApiResponse<DeductionDto>> getDeductionByName(@PathVariable String name) {
        DeductionDto deduction = deductionService.getDeductionByName(name);
        return ResponseEntity.ok(ApiResponse.success("Deduction retrieved successfully", deduction));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<DeductionDto>> createDeduction(@Valid @RequestBody DeductionDto deductionDto) {
        DeductionDto createdDeduction = deductionService.createDeduction(deductionDto);
        return ResponseEntity.ok(ApiResponse.success("Deduction created successfully", createdDeduction));
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<DeductionDto>> updateDeduction(
            @PathVariable String code,
            @Valid @RequestBody DeductionDto deductionDto) {
        DeductionDto updatedDeduction = deductionService.updateDeduction(code, deductionDto);
        return ResponseEntity.ok(ApiResponse.success("Deduction updated successfully", updatedDeduction));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDeduction(@PathVariable String code) {
        deductionService.deleteDeduction(code);
        return ResponseEntity.ok(ApiResponse.success("Deduction deleted successfully"));
    }
}
