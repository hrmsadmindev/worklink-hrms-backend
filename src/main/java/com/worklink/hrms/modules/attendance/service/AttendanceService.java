package com.worklink.hrms.modules.attendance.service;

import com.worklink.hrms.modules.attendance.dto.AttendanceDTO;
import com.worklink.hrms.modules.attendance.dto.BiometricDataDTO;
import com.worklink.hrms.modules.attendance.entity.Attendance;
import com.worklink.hrms.modules.attendance.repository.AttendanceRepository;
import com.worklink.hrms.modules.employee.entity.Employee;
import com.worklink.hrms.modules.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceDTO clockIn(BiometricDataDTO biometricData) {
        try {
            Employee employee = validateEmployee(biometricData.getEmployeeId());
            LocalDate today = LocalDate.now();

            // Check if already clocked in today
            if (attendanceRepository.existsByEmployeeIdAndDate(employee.getId(), today)) {
                Attendance existingAttendance = attendanceRepository
                        .findByEmployeeIdAndDate(employee.getId(), today)
                        .orElseThrow();

                if (existingAttendance.getClockInTime() != null) {
                    throw new RuntimeException("Employee already clocked in today at " +
                            existingAttendance.getClockInTime());
                }
            }

            Attendance attendance = createOrUpdateAttendance(employee, today, biometricData);
            attendance.setClockInTime(LocalTime.now());
            attendance.setStatus(determineAttendanceStatus(attendance));

            attendance = attendanceRepository.save(attendance);

            log.info("Employee {} clocked in at {}", employee.getId(), attendance.getClockInTime());
            return convertToDto(attendance);

        } catch (Exception e) {
            log.error("Error during clock in", e);
            throw new RuntimeException("Clock in failed: " + e.getMessage());
        }
    }

    public AttendanceDTO clockOut(BiometricDataDTO biometricData) {
        try {
            Employee employee = validateEmployee(biometricData.getEmployeeId());
            LocalDate today = LocalDate.now();

            Attendance attendance = attendanceRepository
                    .findByEmployeeIdAndDate(employee.getId(), today)
                    .orElseThrow(() -> new RuntimeException("No clock-in record found for today"));

            if (attendance.getClockOutTime() != null) {
                throw new RuntimeException("Employee already clocked out today at " +
                        attendance.getClockOutTime());
            }

            LocalTime clockOutTime = LocalTime.now();
            attendance.setClockOutTime(clockOutTime);
            attendance.calculateTotalHours();
            attendance.setStatus(determineAttendanceStatus(attendance));

            attendance = attendanceRepository.save(attendance);

            log.info("Employee {} clocked out at {}", employee.getId(), attendance.getClockOutTime());
            return convertToDto(attendance);

        } catch (Exception e) {
            log.error("Error during clock out", e);
            throw new RuntimeException("Clock out failed: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Page<AttendanceDTO> getEmployeeAttendance(Long employeeId, Pageable pageable,
                                                     LocalDate startDate, LocalDate endDate) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }

        Page<Attendance> attendancePage;

        if (startDate != null && endDate != null) {
            attendancePage = attendanceRepository
                    .findByEmployeeIdAndDateBetween(employeeId, startDate, endDate, pageable);
        } else {
            attendancePage = attendanceRepository
                    .findByEmployeeIdOrderByDateDesc(employeeId, pageable);
        }

        return attendancePage.map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<AttendanceDTO> getDailyReport(LocalDate date, Long departmentId) {
        List<Attendance> attendanceList;

        if (departmentId != null) {
            attendanceList = attendanceRepository.findByDateAndEmployeeDepartmentId(date, departmentId);
        } else {
            attendanceList = attendanceRepository.findByDate(date);
        }

        return attendanceList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMonthlyAttendanceSummary(Long employeeId, int month, int year) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }

        List<Attendance> monthlyAttendance = attendanceRepository
                .findMonthlyAttendance(employeeId, month, year);

        Map<String, Object> summary = new HashMap<>();

        // Basic counts
        long totalWorkingDays = monthlyAttendance.size();
        long presentDays = monthlyAttendance.stream()
                .mapToLong(a -> a.getStatus() == Attendance.AttendanceStatus.PRESENT ? 1 : 0)
                .sum();
        long lateDays = monthlyAttendance.stream()
                .mapToLong(a -> a.getStatus() == Attendance.AttendanceStatus.LATE ? 1 : 0)
                .sum();

        // Hours calculation
        double totalHours = monthlyAttendance.stream()
                .mapToDouble(a -> a.getTotalHours() != null ? a.getTotalHours() : 0.0)
                .sum();
        double averageHoursPerDay = totalWorkingDays > 0 ? totalHours / totalWorkingDays : 0.0;

        // Populate summary
        summary.put("totalWorkingDays", totalWorkingDays);
        summary.put("presentDays", presentDays);
        summary.put("absentDays", totalWorkingDays - presentDays);
        summary.put("lateDays", lateDays);
        summary.put("totalHours", Math.round(totalHours * 100.0) / 100.0);
        summary.put("averageHoursPerDay", Math.round(averageHoursPerDay * 100.0) / 100.0);
        summary.put("attendancePercentage", totalWorkingDays > 0 ?
                Math.round((presentDays * 100.0 / totalWorkingDays) * 100.0) / 100.0 : 0.0);

        return summary;
    }

    private Employee validateEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
    }

    private Attendance createOrUpdateAttendance(Employee employee, LocalDate date, BiometricDataDTO biometricData) {
        return attendanceRepository.findByEmployeeIdAndDate(employee.getId(), date)
                .orElseGet(() -> {
                    Attendance attendance = new Attendance();
                    attendance.setEmployee(employee);
                    attendance.setDate(date);
                    return attendance;
                });
    }

    private Attendance.AttendanceStatus determineAttendanceStatus(Attendance attendance) {
        LocalTime clockInTime = attendance.getClockInTime();
        LocalTime clockOutTime = attendance.getClockOutTime();

        // Standard office hours (can be configurable)
        LocalTime standardStartTime = LocalTime.of(9, 0); // 9:00 AM
        LocalTime standardEndTime = LocalTime.of(18, 0);  // 6:00 PM
        LocalTime lateThreshold = LocalTime.of(9, 15);    // 15 minutes grace

        if (clockInTime != null && clockOutTime != null) {
            // Full day attendance
            if (clockInTime.isAfter(lateThreshold)) {
                return Attendance.AttendanceStatus.LATE;
            } else if (clockOutTime.isBefore(standardEndTime.minusMinutes(30))) {
                return Attendance.AttendanceStatus.EARLY_DEPARTURE;
            } else {
                return Attendance.AttendanceStatus.PRESENT;
            }
        } else if (clockInTime != null) {
            // Only clocked in
            return clockInTime.isAfter(lateThreshold) ?
                    Attendance.AttendanceStatus.LATE :
                    Attendance.AttendanceStatus.PRESENT;
        } else {
            return Attendance.AttendanceStatus.ABSENT;
        }
    }

    private AttendanceDTO convertToDto(Attendance attendance) {
        return AttendanceDTO.builder()
                .id(attendance.getId())
                .employeeId(attendance.getEmployee().getId())
                .employeeName(attendance.getEmployee().getFullName())
                .date(attendance.getDate())
                .clockInTime(attendance.getClockInTime())
                .clockOutTime(attendance.getClockOutTime())
                .breakDuration(attendance.getBreakDuration())
                .totalHours(attendance.getTotalHours())
                .status(attendance.getStatus().name())
                .department(attendance.getEmployee().getDepartment() != null ?
                        attendance.getEmployee().getDepartment().getName() : null)
                .build();
    }
}
