package rw.gov.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.gov.payroll.model.Employee;
import rw.gov.payroll.model.PaySlip;
import rw.gov.payroll.model.PaySlip.PaySlipStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaySlipRepository extends JpaRepository<PaySlip, Long> {

    List<PaySlip> findByEmployee(Employee employee);

    List<PaySlip> findByEmployeeAndStatus(Employee employee, PaySlipStatus status);

    List<PaySlip> findByStatus(PaySlipStatus status);

    List<PaySlip> findByMonthAndYear(Integer month, Integer year);

    List<PaySlip> findByMonthAndYearAndStatus(Integer month, Integer year, PaySlipStatus status);

    List<PaySlip> findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);

    Optional<PaySlip> findByEmployeeAndMonthAndYearAndStatus(Employee employee, Integer month, Integer year, PaySlipStatus status);

    boolean existsByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
}
