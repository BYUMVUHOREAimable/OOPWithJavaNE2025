package rw.gov.payroll.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "deductions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deduction {

    @Id
    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "percentage", nullable = false)
    private BigDecimal percentage;

    // Helper method to calculate deduction amount based on base salary
    public BigDecimal calculateAmount(BigDecimal baseSalary) {
        return baseSalary.multiply(percentage).divide(new BigDecimal("100"));
    }
}
