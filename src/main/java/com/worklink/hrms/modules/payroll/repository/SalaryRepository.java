package com.worklink.hrms.modules.payroll.repository;

import com.worklink.hrms.modules.payroll.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {

    List<Salary> findByEmployeeId(Long employeeId);

    List<Salary> findByStatus(Salary.SalaryStatus status);

    @Query("SELECT s FROM Salary s WHERE s.employeeId = :employeeId AND s.status = 'ACTIVE'")
    Optional<Salary> findActiveByEmployeeId(@Param("employeeId") Long employeeId);
}