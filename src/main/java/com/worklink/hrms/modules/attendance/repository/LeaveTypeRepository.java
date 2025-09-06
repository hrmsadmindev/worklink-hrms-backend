package com.worklink.hrms.modules.attendance.repository;

import com.worklink.hrms.modules.attendance.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {

    /**
     * Find leave type by name
     */
    Optional<LeaveType> findByName(String name);

    /**
     * Find leave type by code
     */
    Optional<LeaveType> findByCode(String code);

    /**
     * Find all active leave types
     */
    List<LeaveType> findByIsActiveTrue();

    /**
     * Find all inactive leave types
     */
    List<LeaveType> findByIsActiveFalse();

    /**
     * Find leave types that require approval
     */
    List<LeaveType> findByRequiresApprovalTrue();

    /**
     * Find leave types that don't require approval
     */
    List<LeaveType> findByRequiresApprovalFalse();

    /**
     * Find leave types that allow carryover
     */
    List<LeaveType> findByCarryoverAllowedTrue();

    /**
     * Find leave types by maximum days per year range
     */
    List<LeaveType> findByMaxDaysPerYearBetween(Integer minDays, Integer maxDays);

    /**
     * Find leave types with advance notice requirement
     */
    @Query("SELECT lt FROM LeaveType lt WHERE lt.advanceNoticeDays >= :minNoticeDays")
    List<LeaveType> findByAdvanceNoticeDaysGreaterThanEqual(@Param("minNoticeDays") Integer minNoticeDays);

    /**
     * Find leave types suitable for emergency use (low advance notice)
     */
    @Query("SELECT lt FROM LeaveType lt WHERE lt.advanceNoticeDays <= :maxNoticeDays")
    List<LeaveType> findEmergencyLeaveTypes(@Param("maxNoticeDays") Integer maxNoticeDays);

    /**
     * Find leave types ordered by max days per year (descending)
     */
    List<LeaveType> findAllByOrderByMaxDaysPerYearDesc();

    /**
     * Find leave types ordered by name (ascending)
     */
    List<LeaveType> findAllByOrderByNameAsc();

    /**
     * Check if leave type name already exists (case-insensitive)
     */
    @Query("SELECT CASE WHEN COUNT(lt) > 0 THEN true ELSE false END FROM LeaveType lt " +
            "WHERE LOWER(lt.name) = LOWER(:name)")
    boolean existsByNameIgnoreCase(@Param("name") String name);

    /**
     * Check if leave type code already exists (case-insensitive)
     */
    @Query("SELECT CASE WHEN COUNT(lt) > 0 THEN true ELSE false END FROM LeaveType lt " +
            "WHERE LOWER(lt.code) = LOWER(:code)")
    boolean existsByCodeIgnoreCase(@Param("code") String code);

    /**
     * Get total maximum days across all leave types
     */
    @Query("SELECT COALESCE(SUM(lt.maxDaysPerYear), 0) FROM LeaveType lt WHERE lt.isActive = true")
    Long getTotalMaxDaysForAllLeaveTypes();

    /**
     * Find active leave types with their usage statistics
     */
    @Query("SELECT lt, COUNT(lr.id) as requestCount, COALESCE(SUM(lr.daysRequested), 0) as totalDaysRequested " +
            "FROM LeaveType lt LEFT JOIN lt.leaveRequests lr " +
            "WHERE lt.isActive = true " +
            "GROUP BY lt.id " +
            "ORDER BY requestCount DESC")
    List<Object[]> findActiveLeaveTypesWithUsageStats();

    /**
     * Search leave types by name or description
     */
    @Query("SELECT lt FROM LeaveType lt WHERE " +
            "LOWER(lt.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(lt.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<LeaveType> searchLeaveTypes(@Param("searchTerm") String searchTerm);

    /**
     * Find leave types that need policy review (very high max days)
     */
    @Query("SELECT lt FROM LeaveType lt WHERE lt.maxDaysPerYear > :threshold AND lt.isActive = true")
    List<LeaveType> findLeaveTypesRequiringPolicyReview(@Param("threshold") Integer threshold);
}