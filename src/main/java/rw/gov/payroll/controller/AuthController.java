package rw.gov.payroll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.gov.payroll.dto.ApiResponse;
import rw.gov.payroll.dto.JwtResponse;
import rw.gov.payroll.dto.LoginRequest;
import rw.gov.payroll.dto.RegisterRequest;
import rw.gov.payroll.model.Employee;
import rw.gov.payroll.model.Employee.EmployeeStatus;
import rw.gov.payroll.model.Employment;
import rw.gov.payroll.model.Employment.EmploymentStatus;
import rw.gov.payroll.repository.EmployeeRepository;
import rw.gov.payroll.repository.EmploymentRepository;
import rw.gov.payroll.security.JwtTokenUtil;
import rw.gov.payroll.security.SecurityConstants;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());

        Employee employee = employeeRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + loginRequest.getEmail()));

        return ResponseEntity.ok(ApiResponse.success("Login successful",
                new JwtResponse(jwt, employee.getCode(), employee.getEmail(),
                        employee.getFirstName(), employee.getLastName(), employee.getRoles())));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (employeeRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: Email is already in use!"));
        }

        // Create new employee
        Employee employee = new Employee();
        employee.setCode(generateEmployeeCode());
        employee.setFirstName(registerRequest.getFirstName());
        employee.setLastName(registerRequest.getLastName());
        employee.setEmail(registerRequest.getEmail());
        employee.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        employee.setMobile(registerRequest.getMobile());
        employee.setDateOfBirth(registerRequest.getDateOfBirth());
        employee.setStatus(EmployeeStatus.ACTIVE);

        Set<String> roles = registerRequest.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = new HashSet<>();
            roles.add(SecurityConstants.ROLE_EMPLOYEE);
        }
        employee.setRoles(roles);

        Employee savedEmployee = employeeRepository.save(employee);

        // Create employment if details are provided
        if (registerRequest.getDepartment() != null && registerRequest.getPosition() != null && registerRequest.getBaseSalary() != null) {
            Employment employment = new Employment();
            employment.setCode(generateEmploymentCode());
            employment.setEmployee(savedEmployee);
            employment.setDepartment(registerRequest.getDepartment());
            employment.setPosition(registerRequest.getPosition());
            employment.setBaseSalary(BigDecimal.valueOf(registerRequest.getBaseSalary()));
            employment.setStatus(EmploymentStatus.ACTIVE);
            employment.setJoiningDate(LocalDate.now());
            employmentRepository.save(employment);
        }

        return ResponseEntity.ok(ApiResponse.success("User registered successfully"));
    }

    private String generateEmployeeCode() {
        return "EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateEmploymentCode() {
        return "EMPL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
