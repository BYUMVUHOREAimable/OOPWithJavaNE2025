package rw.gov.payroll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.gov.payroll.dto.PaySlipDto;
import rw.gov.payroll.model.Deduction;
import rw.gov.payroll.model.Employee;
import rw.gov.payroll.model.Employment;
import rw.gov.payroll.model.Message;
import rw.gov.payroll.model.PaySlip;
import rw.gov.payroll.model.PaySlip.PaySlipStatus;
import rw.gov.payroll.repository.DeductionRepository;
import rw.gov.payroll.repository.EmployeeRepository;
import rw.gov.payroll.repository.EmploymentRepository;
import rw.gov.payroll.repository.MessageRepository;
import rw.gov.payroll.repository.PaySlipRepository;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PayrollService {

    @Autowired
    private PaySlipRepository paySlipRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private DeductionRepository deductionRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    /**
     * Get all pay slips
     */
    public List<PaySlipDto> getAllPaySlips() {
        return paySlipRepository.findAll().stream()
                .map(PaySlipDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Get pay slips by status
     */
    public List<PaySlipDto> getPaySlipsByStatus(PaySlipStatus status) {
        return paySlipRepository.findByStatus(status).stream()
                .map(PaySlipDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Get pay slips for a specific employee
     */
    public List<PaySlipDto> getPaySlipsByEmployee(String employeeCode) {
        Employee employee = employeeRepository.findById(employeeCode)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with code: " + employeeCode));

        return paySlipRepository.findByEmployee(employee).stream()
                .map(PaySlipDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Get pay slips for a specific month and year
     */
    public List<PaySlipDto> getPaySlipsByMonthAndYear(Integer month, Integer year) {
        return paySlipRepository.findByMonthAndYear(month, year).stream()
                .map(PaySlipDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific pay slip by ID
     */
    public PaySlipDto getPaySlipById(Long id) {
        PaySlip paySlip = paySlipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pay slip not found with ID: " + id));
        return new PaySlipDto(paySlip);
    }

    /**
     * Generate payroll for all active employees for a specific month and year
     */
    @Transactional
    public List<PaySlipDto> generatePayroll(Integer month, Integer year) {
        // Get all active employments
        List<Employment> activeEmployments = employmentRepository.findByStatus(Employment.EmploymentStatus.ACTIVE);

        // Get all deductions
        List<Deduction> deductions = deductionRepository.findAll();
        Map<String, Deduction> deductionMap = deductions.stream()
                .collect(Collectors.toMap(Deduction::getName, Function.identity()));

        // Check if required deductions exist
        checkRequiredDeductions(deductionMap);

        List<PaySlip> generatedPaySlips = new ArrayList<>();

        for (Employment employment : activeEmployments) {
            Employee employee = employment.getEmployee();

            // Skip if employee is not active
            if (employee.getStatus() != Employee.EmployeeStatus.ACTIVE) {
                continue;
            }

            // Check if pay slip already exists for this employee, month, and year
            if (paySlipRepository.existsByEmployeeAndMonthAndYear(employee, month, year)) {
                continue;
            }

            // Calculate pay slip
            PaySlip paySlip = calculatePaySlip(employee, employment, deductionMap, month, year);
            generatedPaySlips.add(paySlip);
        }

        // Save all generated pay slips
        List<PaySlip> savedPaySlips = paySlipRepository.saveAll(generatedPaySlips);

        return savedPaySlips.stream()
                .map(PaySlipDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Approve a pay slip (change status from PENDING to PAID)
     */
    @Transactional
    public PaySlipDto approvePaySlip(Long id) {
        PaySlip paySlip = paySlipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pay slip not found with ID: " + id));

        if (paySlip.getStatus() == PaySlipStatus.PAID) {
            throw new IllegalStateException("Pay slip is already approved");
        }

        paySlip.setStatus(PaySlipStatus.PAID);
        PaySlip savedPaySlip = paySlipRepository.save(paySlip);

        // Message generation is handled by database trigger
        // Wait a moment for the database trigger to execute
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Find the message that was just created by the trigger
        List<Message> messages = messageRepository.findByEmployeeAndMonthAndYear(
                savedPaySlip.getEmployee(), 
                savedPaySlip.getMonth(), 
                savedPaySlip.getYear()
        );

        // Send email for each message
        System.out.println("Found " + messages.size() + " messages for approved pay slip ID: " + id);
        for (Message message : messages) {
            if (!message.getSent()) {
                System.out.println("Sending message ID: " + message.getId() + " for pay slip ID: " + id);
                try {
                    messageService.sendEmailNotification(message);
                    System.out.println("Successfully sent message ID: " + message.getId() + " for pay slip ID: " + id);
                } catch (Exception e) {
                    System.err.println("Error sending message ID: " + message.getId() + " - " + e.getMessage());
                }
            }
        }

        return new PaySlipDto(savedPaySlip);
    }

    /**
     * Approve all pending pay slips for a specific month and year
     */
    @Transactional
    public List<PaySlipDto> approveAllPaySlips(Integer month, Integer year) {
        List<PaySlip> pendingPaySlips = paySlipRepository.findByMonthAndYearAndStatus(month, year, PaySlipStatus.PENDING);
        System.out.println("Approving " + pendingPaySlips.size() + " pending pay slips for " + month + "/" + year);

        pendingPaySlips.forEach(paySlip -> paySlip.setStatus(PaySlipStatus.PAID));

        List<PaySlip> savedPaySlips = paySlipRepository.saveAll(pendingPaySlips);

        // Message generation is handled by database trigger for each updated pay slip
        // Wait a moment for the database trigger to execute
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Find the messages that were just created by the trigger
        List<Message> messages = messageRepository.findByMonthAndYear(month, year);
        System.out.println("Found " + messages.size() + " messages for approved pay slips for " + month + "/" + year);

        // Send email for each message
        int sentCount = 0;
        for (Message message : messages) {
            if (!message.getSent()) {
                System.out.println("Sending message ID: " + message.getId() + " for " + month + "/" + year);
                try {
                    messageService.sendEmailNotification(message);
                    System.out.println("Successfully sent message ID: " + message.getId());
                    sentCount++;
                } catch (Exception e) {
                    System.err.println("Error sending message ID: " + message.getId() + " - " + e.getMessage());
                }
            }
        }
        System.out.println("Successfully sent " + sentCount + " messages for " + month + "/" + year);

        return savedPaySlips.stream()
                .map(PaySlipDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Calculate a pay slip for an employee
     */
    private PaySlip calculatePaySlip(Employee employee, Employment employment, Map<String, Deduction> deductionMap, Integer month, Integer year) {
        BigDecimal baseSalary = employment.getBaseSalary();

        // Get deduction percentages
        BigDecimal employeeTaxRate = deductionMap.get("EmployeeTax").getPercentage().divide(new BigDecimal("100"));
        BigDecimal pensionRate = deductionMap.get("Pension").getPercentage().divide(new BigDecimal("100"));
        BigDecimal medicalInsuranceRate = deductionMap.get("MedicalInsurance").getPercentage().divide(new BigDecimal("100"));
        BigDecimal otherTaxRate = deductionMap.get("Others").getPercentage().divide(new BigDecimal("100"));
        BigDecimal housingRate = deductionMap.get("Housing").getPercentage().divide(new BigDecimal("100"));
        BigDecimal transportRate = deductionMap.get("Transport").getPercentage().divide(new BigDecimal("100"));

        // Calculate housing and transport amounts
        BigDecimal houseAmount = baseSalary.multiply(housingRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal transportAmount = baseSalary.multiply(transportRate).setScale(2, RoundingMode.HALF_UP);

        // Calculate gross salary (base + housing + transport)
        BigDecimal grossSalary = baseSalary.add(houseAmount).add(transportAmount).setScale(2, RoundingMode.HALF_UP);

        // Calculate deductions based on base salary as per the example in the requirements
        BigDecimal employeeTaxAmount = baseSalary.multiply(employeeTaxRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal pensionAmount = baseSalary.multiply(pensionRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal medicalInsuranceAmount = baseSalary.multiply(medicalInsuranceRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal otherTaxAmount = baseSalary.multiply(otherTaxRate).setScale(2, RoundingMode.HALF_UP);

        // Calculate total deductions and net salary
        BigDecimal totalDeductions = employeeTaxAmount.add(pensionAmount).add(medicalInsuranceAmount).add(otherTaxAmount);
        BigDecimal netSalary = grossSalary.subtract(totalDeductions).setScale(2, RoundingMode.HALF_UP);

        // Create pay slip
        PaySlip paySlip = new PaySlip();
        paySlip.setEmployee(employee);
        paySlip.setBaseSalary(baseSalary);
        paySlip.setHouseAmount(houseAmount);
        paySlip.setTransportAmount(transportAmount);
        paySlip.setEmployeeTaxAmount(employeeTaxAmount);
        paySlip.setPensionAmount(pensionAmount);
        paySlip.setMedicalInsuranceAmount(medicalInsuranceAmount);
        paySlip.setOtherTaxAmount(otherTaxAmount);
        paySlip.setGrossSalary(grossSalary);
        paySlip.setNetSalary(netSalary);
        paySlip.setMonth(month);
        paySlip.setYear(year);
        paySlip.setStatus(PaySlipStatus.PENDING);

        return paySlip;
    }

    /**
     * Check if all required deductions exist
     */
    private void checkRequiredDeductions(Map<String, Deduction> deductionMap) {
        List<String> requiredDeductions = List.of("EmployeeTax", "Pension", "MedicalInsurance", "Others", "Housing", "Transport");

        for (String deductionName : requiredDeductions) {
            if (!deductionMap.containsKey(deductionName)) {
                throw new IllegalStateException("Required deduction not found: " + deductionName);
            }
        }
    }
}
