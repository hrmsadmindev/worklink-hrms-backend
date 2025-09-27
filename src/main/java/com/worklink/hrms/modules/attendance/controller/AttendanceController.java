package com.worklink.hrms.modules.attendance.controller;

import com.worklink.hrms.modules.attendance.dto.AttendanceDTO;
import com.worklink.hrms.modules.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Slf4j
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/clock-in")
    public ResponseEntity<AttendanceDTO> clockIn(@Valid @RequestBody AttendanceDTO attendanceData) {
        try {
            log.info("Clock-in request received for employee: {}", attendanceData.getEmployeeId());
            AttendanceDTO attendance = attendanceService.clockIn(attendanceData);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            log.error("Clock-in failed", e);
            throw new RuntimeException("Clock-in failed: " + e.getMessage());
        }
    }

    @PostMapping("/clock-out")
    public ResponseEntity<AttendanceDTO> clockOut(@Valid @RequestBody AttendanceDTO attendanceData) {
        try {
            log.info("Clock-out request received for employee: {}", attendanceData.getEmployeeId());
            AttendanceDTO attendance = attendanceService.clockOut(attendanceData);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            log.error("Clock-out failed", e);
            throw new RuntimeException("Clock-out failed: " + e.getMessage());
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceDTO>> getEmployeeAttendance(
            @PathVariable Long employeeId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<AttendanceDTO> attendance = attendanceService.getEmployeeAttendance(employeeId, startDate, endDate);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/daily-report")
    public ResponseEntity<List<AttendanceDTO>> getDailyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long departmentId) {

        List<AttendanceDTO> report = attendanceService.getDailyReport(date, departmentId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/monthly-summary/{employeeId}")
    public ResponseEntity<Map<String, Object>> getMonthlyAttendanceSummary(
            @PathVariable Long employeeId,
            @RequestParam int month,
            @RequestParam int year) {

        Map<String, Object> summary = attendanceService.getMonthlyAttendanceSummary(employeeId, month, year);
        return ResponseEntity.ok(summary);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        log.error("Attendance API error", e);
        Map<String, String> error = Map.of(
                "error", e.getMessage(),
                "timestamp", java.time.Instant.now().toString()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
