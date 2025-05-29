package rw.gov.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.gov.payroll.model.Employee;
import rw.gov.payroll.model.Employment;
import rw.gov.payroll.model.Employment.EmploymentStatus;

import java.util.List;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, String> {
    
    List<Employment> findByEmployee(Employee employee);
    
    List<Employment> findByEmployeeAndStatus(Employee employee, EmploymentStatus status);
    
    List<Employment> findByStatus(EmploymentStatus status);
}