// LeaveBalanceRepository.java
package com.worklink.hrms.modules.attendance.repository;

import com.worklink.hrms.modules.attendance.entity.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    /**
     * Find leave balance by employee, leave type, and year
     */
    Optional<LeaveBalance> findByEmployeeIdAndLeaveTypeIdAndYear(Long employeeId, Long leaveTypeId, Integer year);

    /**
     * Find all leave balances for an employee in a specific year
     */
    List<LeaveBalance> findByEmployeeIdAndYear(Long employeeId, Integer year);

    /**
     * Find all leave balances for a specific year (for all employees)
     */
    List<LeaveBalance> findByYear(Integer year);

    /**
     * Find all leave balances for an employee across all years
     */
    List<LeaveBalance> findByEmployeeIdOrderByYearDesc(Long employeeId);

    /**
     * Find all leave balances for a specific leave type in a year
     */
    List<LeaveBalance> findByLeaveTypeIdAndYear(Long leaveTypeId, Integer year);

    /**
     * Find leave balances with remaining days greater than specified amount
     */
    @Query("SELECT lb FROM LeaveBalance lb WHERE lb.employee.id = :employeeId " +
            "AND lb.year = :year AND (lb.allocatedDays - lb.usedDays - lb.balance) > :minRemainingDays")
    List<LeaveBalance> findByEmployeeIdAndYearWithRemainingDaysGreaterThan(
            @Param("employeeId") Long employeeId,
            @Param("year") Integer year,
            @Param("minRemainingDays") Integer minRemainingDays);

    /**
     * Find employees with low leave balance (for notifications)
     */
    @Query("SELECT lb FROM LeaveBalance lb WHERE lb.year = :year " +
            "AND (lb.allocatedDays - lb.usedDays - lb.balance) <= :lowBalanceThreshold")
    List<LeaveBalance> findEmployeesWithLowBalance(@Param("year") Integer year, @Param("lowBalanceThreshold") Integer lowBalanceThreshold);

    /**
     * Get total allocated days for a leave type across all employees in a year
     */
    @Query("SELECT COALESCE(SUM(lb.allocatedDays), 0) FROM LeaveBalance lb " +
            "WHERE lb.leaveType.id = :leaveTypeId AND lb.year = :year")
    Long getTotalAllocatedDaysByLeaveTypeAndYear(@Param("leaveTypeId") Long leaveTypeId, @Param("year") Integer year);

    /**
     * Get total used days for a leave type across all employees in a year
     */
    @Query("SELECT COALESCE(SUM(lb.usedDays), 0) FROM LeaveBalance lb " +
            "WHERE lb.leaveType.id = :leaveTypeId AND lb.year = :year")
    Long getTotalUsedDaysByLeaveTypeAndYear(@Param("leaveTypeId") Long leaveTypeId, @Param("year") Integer year);

    /**
     * Find leave balances by department (through employee relationship)
     */
    @Query("SELECT lb FROM LeaveBalance lb JOIN lb.employee e " +
            "WHERE e.departmentId = :departmentId AND lb.year = :year")
    List<LeaveBalance> findByDepartmentIdAndYear(@Param("departmentId") Long departmentId, @Param("year") Integer year);

    /**
     * Find leave balances that need carryover processing
     * (balances where remaining days > 0 and carryover is allowed)
     */
//    @Query("SELECT lb FROM LeaveBalance lb JOIN lb.leaveType lt " +
//            "WHERE lb.year = :year AND lt.carryoverAllowed = true " +
//            "AND (lb.allocatedDays + lb.carryoverDays - lb.usedDays - lb.balance) > 0")
//    List<LeaveBalance> findBalancesForCarryoverProcessing(@Param("year") Integer year);

    /**
     * Check if employee has any leave balance for a specific year
     */
    boolean existsByEmployeeIdAndYear(Long employeeId, Integer year);

    /**
     * Delete all leave balances for an employee (for cleanup)
     */
    void deleteByEmployeeId(Long employeeId);

    /**
     * Delete leave balances for a specific year (for year-end cleanup)
     */
    void deleteByYear(Integer year);
}