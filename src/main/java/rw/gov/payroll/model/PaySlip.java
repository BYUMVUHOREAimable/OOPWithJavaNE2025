package rw.gov.payroll.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pay_slips")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaySlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_code", nullable = false)
    private Employee employee;

    @Column(name = "house_amount", nullable = false)
    private BigDecimal houseAmount;

    @Column(name = "transport_amount", nullable = false)
    private BigDecimal transportAmount;

    @Column(name = "employee_tax_amount", nullable = false)
    private BigDecimal employeeTaxAmount;

    @Column(name = "pension_amount", nullable = false)
    private BigDecimal pensionAmount;

    @Column(name = "medical_insurance_amount", nullable = false)
    private BigDecimal medicalInsuranceAmount;

    @Column(name = "other_tax_amount", nullable = false)
    private BigDecimal otherTaxAmount;

    @Column(name = "gross_salary", nullable = false)
    private BigDecimal grossSalary;

    @Column(name = "net_salary", nullable = false)
    private BigDecimal netSalary;

    @Column(name = "month", nullable = false)
    private Integer month;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaySlipStatus status;

    @Column(name = "base_salary", nullable = false)
    private BigDecimal baseSalary;

    public enum PaySlipStatus {
        PENDING, PAID
    }
}
