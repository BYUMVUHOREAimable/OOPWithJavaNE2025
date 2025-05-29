package rw.gov.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.gov.payroll.model.Deduction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeductionDto {

    private String code;

    @NotBlank(message = "Deduction name is required")
    @Size(min = 2, max = 50, message = "Deduction name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Percentage is required")
    @Positive(message = "Percentage must be positive")
    private BigDecimal percentage;

    // Constructor to convert from Entity to DTO
    public DeductionDto(Deduction deduction) {
        this.code = deduction.getCode();
        this.name = deduction.getName();
        this.percentage = deduction.getPercentage();
    }

    // Convert DTO to Entity
    public Deduction toEntity() {
        Deduction deduction = new Deduction();
        deduction.setCode(this.code);
        deduction.setName(this.name);
        deduction.setPercentage(this.percentage);
        return deduction;
    }
}
