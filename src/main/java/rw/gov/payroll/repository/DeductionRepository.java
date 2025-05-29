package rw.gov.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.gov.payroll.model.Deduction;

import java.util.Optional;

@Repository
public interface DeductionRepository extends JpaRepository<Deduction, String> {
    
    Optional<Deduction> findByName(String name);
    
    boolean existsByName(String name);
}