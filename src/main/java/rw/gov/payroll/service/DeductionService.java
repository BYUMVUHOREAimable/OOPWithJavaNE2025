package rw.gov.payroll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rw.gov.payroll.dto.DeductionDto;
import rw.gov.payroll.model.Deduction;
import rw.gov.payroll.repository.DeductionRepository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeductionService {

    @Autowired
    private DeductionRepository deductionRepository;

    public List<DeductionDto> getAllDeductions() {
        return deductionRepository.findAll().stream()
                .map(DeductionDto::new)
                .collect(Collectors.toList());
    }

    public DeductionDto getDeductionByCode(String code) {
        Deduction deduction = deductionRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Deduction not found with code: " + code));
        return new DeductionDto(deduction);
    }

    public DeductionDto getDeductionByName(String name) {
        Deduction deduction = deductionRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Deduction not found with name: " + name));
        return new DeductionDto(deduction);
    }

    public DeductionDto createDeduction(DeductionDto deductionDto) {
        if (deductionRepository.existsByName(deductionDto.getName())) {
            throw new IllegalArgumentException("Deduction with name " + deductionDto.getName() + " already exists");
        }

        Deduction deduction = deductionDto.toEntity();

        // Generate deduction code if not provided
        if (deduction.getCode() == null || deduction.getCode().isEmpty()) {
            deduction.setCode(generateDeductionCode());
        }

        Deduction savedDeduction = deductionRepository.save(deduction);
        return new DeductionDto(savedDeduction);
    }

    public DeductionDto updateDeduction(String code, DeductionDto deductionDto) {
        Deduction existingDeduction = deductionRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Deduction not found with code: " + code));

        // Check if name is being changed and if it's already in use
        if (!existingDeduction.getName().equals(deductionDto.getName()) && 
                deductionRepository.existsByName(deductionDto.getName())) {
            throw new IllegalArgumentException("Deduction with name " + deductionDto.getName() + " already exists");
        }

        // Update fields
        existingDeduction.setName(deductionDto.getName());
        existingDeduction.setPercentage(deductionDto.getPercentage());

        Deduction updatedDeduction = deductionRepository.save(existingDeduction);
        return new DeductionDto(updatedDeduction);
    }

    public void deleteDeduction(String code) {
        if (!deductionRepository.existsById(code)) {
            throw new EntityNotFoundException("Deduction not found with code: " + code);
        }
        deductionRepository.deleteById(code);
    }

    private String generateDeductionCode() {
        return "DED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Initialize default deductions when the application starts
     */
    @PostConstruct
    public void initDefaultDeductions() {
        // Only initialize if no deductions exist
        if (deductionRepository.count() == 0) {
            createDefaultDeduction("EmployeeTax", new BigDecimal("30"));
            createDefaultDeduction("Pension", new BigDecimal("6"));
            createDefaultDeduction("MedicalInsurance", new BigDecimal("5"));
            createDefaultDeduction("Others", new BigDecimal("5"));
            createDefaultDeduction("Housing", new BigDecimal("14"));
            createDefaultDeduction("Transport", new BigDecimal("14"));
        }
    }

    private void createDefaultDeduction(String name, BigDecimal percentage) {
        Deduction deduction = new Deduction();
        deduction.setCode(generateDeductionCode());
        deduction.setName(name);
        deduction.setPercentage(percentage);
        deductionRepository.save(deduction);
    }
}
