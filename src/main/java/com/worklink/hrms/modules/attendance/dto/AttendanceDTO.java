package com.worklink.hrms.modules.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeName;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private LocalTime clockInTime;
    private LocalTime clockOutTime;
    private Integer breakDuration;
    private Double totalHours;
    private String status;
    private String location;
    private String department;
    private String deviceId;
    private String notes;
}