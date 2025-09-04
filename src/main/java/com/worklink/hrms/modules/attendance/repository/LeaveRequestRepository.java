package com.worklink.hrms.modules.attendance.repository;

import com.worklink.hrms.modules.attendance.entity.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    Page<LeaveRequest> findByEmployeeIdOrderByAppliedDateDesc(Long employeeId, Pageable pageable);

    Page<LeaveRequest> findByManagerIdAndStatusOrderByAppliedDateDesc(Long managerId,
                                                                      LeaveRequest.LeaveStatus status,
                                                                      Pageable pageable);

    List<LeaveRequest> findByEmployeeIdAndStatus(Long employeeId, LeaveRequest.LeaveStatus status);

    List<LeaveRequest> findByStatus(LeaveRequest.LeaveStatus status);

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.id = :employeeId " +
            "AND lr.startDate <= :endDate AND lr.endDate >= :startDate " +
            "AND lr.status IN ('APPROVED', 'PENDING')")
    List<LeaveRequest> findOverlappingLeaves(@Param("employeeId") Long employeeId,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.department.id = :departmentId " +
            "AND lr.startDate <= :endDate AND lr.endDate >= :startDate " +
            "AND lr.status = 'APPROVED'")
    List<LeaveRequest> findDepartmentLeavesInPeriod(@Param("departmentId") Long departmentId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(lr.daysRequested) FROM LeaveRequest lr WHERE lr.employee.id = :employeeId " +
            "AND lr.leaveType.id = :leaveTypeId AND lr.status = 'APPROVED' " +
            "AND YEAR(lr.startDate) = :year")
    Integer sumApprovedLeavesByEmployeeAndTypeAndYear(@Param("employeeId") Long employeeId,
                                                      @Param("leaveTypeId") Long leaveTypeId,
                                                      @Param("year") int year);

    @Query("SELECT COUNT(lr) FROM LeaveRequest lr WHERE lr.manager.id = :managerId " +
            "AND lr.status = 'PENDING'")
    Long countPendingApprovalsByManager(@Param("managerId") Long managerId);
}