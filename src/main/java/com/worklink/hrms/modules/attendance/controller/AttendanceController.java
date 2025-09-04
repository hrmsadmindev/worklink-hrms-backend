package com.worklink.hrms.modules.attendance.controller;

import com.worklink.hrms.modules.attendance.dto.AttendanceDTO;
import com.worklink.hrms.modules.attendance.dto.BiometricDataDTO;
import com.worklink.hrms.modules.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<AttendanceDTO> clockIn(@Valid @RequestBody BiometricDataDTO biometricData) {
        try {
            log.info("Clock-in request received for employee: {}", biometricData.getEmployeeId());
            AttendanceDTO attendance = attendanceService.clockIn(biometricData);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            log.error("Clock-in failed", e);
            throw new RuntimeException("Clock-in failed: " + e.getMessage());
        }
    }

    @PostMapping("/clock-out")
    public ResponseEntity<AttendanceDTO> clockOut(@Valid @RequestBody BiometricDataDTO biometricData) {
        try {
            log.info("Clock-out request received for employee: {}", biometricData.getEmployeeId());
            AttendanceDTO attendance = attendanceService.clockOut(biometricData);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            log.error("Clock-out failed", e);
            throw new RuntimeException("Clock-out failed: " + e.getMessage());
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<AttendanceDTO>> getEmployeeAttendance(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AttendanceDTO> attendance = attendanceService.getEmployeeAttendance(
                employeeId, pageable, startDate, endDate);
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