package com.worklink.hrms.modules.attendance.repository;

import com.worklink.hrms.modules.attendance.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    List<Attendance> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    Page<Attendance> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate,
                                                    LocalDate endDate, Pageable pageable);

    Page<Attendance> findByEmployeeIdOrderByDateDesc(Long employeeId, Pageable pageable);

    List<Attendance> findByDateAndEmployeeDepartmentId(LocalDate date, Long departmentId);

    List<Attendance> findByDate(LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.employee.id = :employeeId " +
            "AND MONTH(a.date) = :month AND YEAR(a.date) = :year ORDER BY a.date")
    List<Attendance> findMonthlyAttendance(@Param("employeeId") Long employeeId,
                                           @Param("month") int month,
                                           @Param("year") int year);

//    @Query("SELECT a FROM Attendance a WHERE a.employee.department.id = :departmentId " +
//            "AND a.date BETWEEN :startDate AND :endDate")
//    List<Attendance> findDepartmentAttendance(@Param("departmentId") Long departmentId,
//                                              @Param("startDate") LocalDate startDate,
//                                              @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.employee.id = :employeeId " +
            "AND a.status = :status AND MONTH(a.date) = :month AND YEAR(a.date) = :year")
    Long countByEmployeeAndStatusAndMonth(@Param("employeeId") Long employeeId,
                                          @Param("status") Attendance.AttendanceStatus status,
                                          @Param("month") int month,
                                          @Param("year") int year);

    @Query("SELECT SUM(a.totalHours) FROM Attendance a WHERE a.employee.id = :employeeId " +
            "AND MONTH(a.date) = :month AND YEAR(a.date) = :year")
    Double sumTotalHoursByEmployeeAndMonth(@Param("employeeId") Long employeeId,
                                           @Param("month") int month,
                                           @Param("year") int year);

    @Query("SELECT a FROM Attendance a WHERE a.status = 'LATE' AND a.date = :date")
    List<Attendance> findLateArrivals(@Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.clockOutTime IS NULL AND a.date = :date")
    List<Attendance> findIncompleteAttendance(@Param("date") LocalDate date);

    boolean existsByEmployeeIdAndDate(Long employeeId, LocalDate date);
}