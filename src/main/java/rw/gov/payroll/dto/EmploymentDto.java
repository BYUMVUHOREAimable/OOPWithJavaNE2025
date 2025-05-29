package rw.gov.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.gov.payroll.model.Employment;
import rw.gov.payroll.model.Employment.EmploymentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDto {
    private String code;

    private String employeeCode;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Position is required")
    private String position;

    @NotNull(message = "Base salary is required")
    @Positive(message = "Base salary must be positive")
    private BigDecimal baseSalary;

    private EmploymentStatus status;

    private LocalDate joiningDate;

    // Constructor to convert from Entity to DTO
    public EmploymentDto(Employment employment) {
        this.code = employment.getCode();
        this.employeeCode = employment.getEmployee().getCode();
        this.department = employment.getDepartment();
        this.position = employment.getPosition();
        this.baseSalary = employment.getBaseSalary();
        this.status = employment.getStatus();
        this.joiningDate = employment.getJoiningDate();
    }
}
