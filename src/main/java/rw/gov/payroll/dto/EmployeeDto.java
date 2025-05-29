package rw.gov.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.gov.payroll.model.Employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private String code;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String mobile;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private Employee.EmployeeStatus status;

    private Set<String> roles;

    // Constructor to convert from Entity to DTO
    public EmployeeDto(Employee employee) {
        this.code = employee.getCode();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        this.mobile = employee.getMobile();
        this.dateOfBirth = employee.getDateOfBirth();
        this.status = employee.getStatus();
        this.roles = employee.getRoles();
    }

    // Convert DTO to Entity
    public Employee toEntity() {
        Employee employee = new Employee();
        employee.setCode(this.code);
        employee.setFirstName(this.firstName);
        employee.setLastName(this.lastName);
        employee.setEmail(this.email);
        employee.setMobile(this.mobile);
        employee.setDateOfBirth(this.dateOfBirth);
        employee.setStatus(this.status != null ? this.status : Employee.EmployeeStatus.ACTIVE);
        employee.setRoles(this.roles);
        return employee;
    }
}
