package rw.gov.payroll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rw.gov.payroll.dto.ApiResponse;
import rw.gov.payroll.dto.PaySlipDto;
import rw.gov.payroll.model.PaySlip.PaySlipStatus;
import rw.gov.payroll.service.EmployeeService;
import rw.gov.payroll.service.PayrollService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/payslips")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<PaySlipDto>>> getAllPaySlips() {
        List<PaySlipDto> paySlips = payrollService.getAllPaySlips();
        return ResponseEntity.ok(ApiResponse.success("Pay slips retrieved successfully", paySlips));
    }

    @GetMapping("/payslips/status/{status}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<PaySlipDto>>> getPaySlipsByStatus(@PathVariable PaySlipStatus status) {
        List<PaySlipDto> paySlips = payrollService.getPaySlipsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Pay slips retrieved successfully", paySlips));
    }

    @GetMapping("/payslips/employee/{employeeCode}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN') or @securityService.isCurrentUser(#employeeCode)")
    public ResponseEntity<ApiResponse<List<PaySlipDto>>> getPaySlipsByEmployee(@PathVariable String employeeCode) {
        List<PaySlipDto> paySlips = payrollService.getPaySlipsByEmployee(employeeCode);
        return ResponseEntity.ok(ApiResponse.success("Pay slips retrieved successfully", paySlips));
    }

    @GetMapping("/payslips/month/{month}/year/{year}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<PaySlipDto>>> getPaySlipsByMonthAndYear(
            @PathVariable @Min(1) @Max(12) Integer month,
            @PathVariable @Min(2000) Integer year) {
        List<PaySlipDto> paySlips = payrollService.getPaySlipsByMonthAndYear(month, year);
        return ResponseEntity.ok(ApiResponse.success("Pay slips retrieved successfully", paySlips));
    }

    @GetMapping("/payslips/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN') or @securityService.isPaySlipOwner(#id)")
    public ResponseEntity<ApiResponse<PaySlipDto>> getPaySlipById(@PathVariable Long id) {
        PaySlipDto paySlip = payrollService.getPaySlipById(id);
        return ResponseEntity.ok(ApiResponse.success("Pay slip retrieved successfully", paySlip));
    }

    @GetMapping("/payslips/me")
    public ResponseEntity<ApiResponse<List<PaySlipDto>>> getMyPaySlips() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String employeeCode = employeeService.getEmployeeByEmail(email).getCode();
        List<PaySlipDto> paySlips = payrollService.getPaySlipsByEmployee(employeeCode);
        return ResponseEntity.ok(ApiResponse.success("Pay slips retrieved successfully", paySlips));
    }

    @PostMapping("/generate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<PaySlipDto>>> generatePayroll(
            @RequestParam @Min(1) @Max(12) Integer month,
            @RequestParam @Min(2000) Integer year) {
        List<PaySlipDto> generatedPaySlips = payrollService.generatePayroll(month, year);
        return ResponseEntity.ok(ApiResponse.success("Payslips generated successfully", generatedPaySlips));
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<PaySlipDto>> approvePaySlip(@PathVariable Long id) {
        PaySlipDto approvedPaySlip = payrollService.approvePaySlip(id);
        return ResponseEntity.ok(ApiResponse.success("Pay slip approved successfully", approvedPaySlip));
    }

    @PutMapping("/approve/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<PaySlipDto>>> approveAllPaySlips(
            @RequestParam @Min(1) @Max(12) Integer month,
            @RequestParam @Min(2000) Integer year) {
        List<PaySlipDto> approvedPaySlips = payrollService.approveAllPaySlips(month, year);
        return ResponseEntity.ok(ApiResponse.success("All pay slips approved successfully", approvedPaySlips));
    }
}
