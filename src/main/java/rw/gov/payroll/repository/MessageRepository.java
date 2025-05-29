package rw.gov.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.gov.payroll.model.Employee;
import rw.gov.payroll.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    List<Message> findByEmployee(Employee employee);
    
    List<Message> findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
    
    List<Message> findByMonthAndYear(Integer month, Integer year);
    
    List<Message> findBySent(Boolean sent);
}