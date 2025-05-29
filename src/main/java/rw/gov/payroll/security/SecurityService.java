package rw.gov.payroll.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rw.gov.payroll.model.Employee;
import rw.gov.payroll.model.PaySlip;
import rw.gov.payroll.repository.EmployeeRepository;
import rw.gov.payroll.repository.PaySlipRepository;

@Service
public class SecurityService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PaySlipRepository paySlipRepository;

    /**
     * Checks if the currently authenticated user is the same as the employee with the given code
     * 
     * @param employeeCode The code of the employee to check
     * @return true if the current user is the same as the employee with the given code, false otherwise
     */
    public boolean isCurrentUser(String employeeCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUserEmail = authentication.getName();
        return employeeRepository.findById(employeeCode)
                .map(employee -> employee.getEmail().equals(currentUserEmail))
                .orElse(false);
    }

    /**
     * Checks if the currently authenticated user has any of the specified roles
     * 
     * @param roles The roles to check
     * @return true if the current user has any of the specified roles, false otherwise
     */
    public boolean hasAnyRole(String... roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUserEmail = authentication.getName();
        Employee employee = employeeRepository.findByEmail(currentUserEmail).orElse(null);

        if (employee == null) {
            return false;
        }

        for (String role : roles) {
            if (employee.getRoles().contains(role)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the currently authenticated user is the owner of the pay slip with the given ID
     * 
     * @param paySlipId The ID of the pay slip to check
     * @return true if the current user is the owner of the pay slip, false otherwise
     */
    public boolean isPaySlipOwner(Long paySlipId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUserEmail = authentication.getName();
        Employee employee = employeeRepository.findByEmail(currentUserEmail).orElse(null);

        if (employee == null) {
            return false;
        }

        return paySlipRepository.findById(paySlipId)
                .map(paySlip -> paySlip.getEmployee().getCode().equals(employee.getCode()))
                .orElse(false);
    }
}
