package com.worklink.hrms.modules.payroll.repository;

import com.worklink.hrms.modules.payroll.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    List<Payroll> findByEmployeeId(Long employeeId);

    List<Payroll> findByStatus(Payroll.PayrollStatus status);

    Optional<Payroll> findByEmployeeIdAndPayPeriodMonthAndPayPeriodYear(
        Long employeeId, Integer month, Integer year);

    List<Payroll> findByPayPeriodMonthAndPayPeriodYear(Integer month, Integer year);
}