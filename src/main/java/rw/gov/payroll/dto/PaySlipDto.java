package rw.gov.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.gov.payroll.model.PaySlip;
import rw.gov.payroll.model.PaySlip.PaySlipStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaySlipDto {

    private Long id;

    private String empId;

    private String name;

    private BigDecimal base;

    private BigDecimal housing;

    private BigDecimal transport;

    private BigDecimal tax;

    private BigDecimal pension;

    private BigDecimal medical;

    private BigDecimal others;

    private BigDecimal gross;

    private BigDecimal net;

    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;

    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be after 2000")
    private Integer year;

    private PaySlipStatus status;

    // Constructor to convert from Entity to DTO
    public PaySlipDto(PaySlip paySlip) {
        this.id = paySlip.getId();
        this.empId = paySlip.getEmployee().getCode();
        this.name = paySlip.getEmployee().getFirstName() + " " + paySlip.getEmployee().getLastName();
        this.base = paySlip.getBaseSalary();
        this.housing = paySlip.getHouseAmount();
        this.transport = paySlip.getTransportAmount();
        this.tax = paySlip.getEmployeeTaxAmount();
        this.pension = paySlip.getPensionAmount();
        this.medical = paySlip.getMedicalInsuranceAmount();
        this.others = paySlip.getOtherTaxAmount();
        this.gross = paySlip.getGrossSalary();
        this.net = paySlip.getNetSalary();
        this.month = paySlip.getMonth();
        this.year = paySlip.getYear();
        this.status = paySlip.getStatus();
    }
}
