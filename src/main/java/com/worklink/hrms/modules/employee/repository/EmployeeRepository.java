package com.worklink.hrms.modules.employee.repository;

import com.worklink.hrms.modules.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByStatus(Employee.EmployeeStatus status);

    @Query("SELECT e FROM Employee e WHERE e.firstName LIKE %:keyword% OR e.lastName LIKE %:keyword% OR e.email LIKE %:keyword%")
    List<Employee> searchEmployees(@Param("keyword") String keyword);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.status = :status")
    Long countByStatus(@Param("status") Employee.EmployeeStatus status);

    //Custom queries to get active employees only
    // Get all active employees
    @Query("SELECT e FROM Employee e JOIN User u ON e.id = u.employeeId WHERE u.status = 'ACTIVE'")
    List<Employee> findAllActive();

    // Get active employee by ID
    @Query("SELECT e FROM Employee e JOIN User u ON e.id = u.employeeId WHERE e.id = :id AND u.status = 'ACTIVE'")
    Optional<Employee> findActiveById(@Param("id") Long id);

    // Search active employees
    @Query("SELECT e FROM Employee e JOIN User u ON e.id = u.employeeId WHERE u.status = 'ACTIVE' AND " +
            "(LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Employee> findActiveByKeyword(@Param("keyword") String keyword);

    // Get active employees by department
    @Query("SELECT e FROM Employee e JOIN User u ON e.id = u.employeeId WHERE u.status = 'ACTIVE' AND e.departmentId = :departmentId")
    List<Employee> findActiveByDepartmentId(@Param("departmentId") Long departmentId);

    // Check if employee exists and is active
    @Query("SELECT COUNT(e) > 0 FROM Employee e JOIN User u ON e.id = u.employeeId WHERE e.id = :id AND u.status = 'ACTIVE'")
    boolean existsByIdAndActive(@Param("id") Long id);
}