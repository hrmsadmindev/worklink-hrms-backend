package com.worklink.hrms.modules.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestDTO {
    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeName;

    @NotNull(message = "Leave type ID is required")
    private Long leaveTypeId;

    private String leaveTypeName;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Positive(message = "Days requested must be positive")
    private Integer daysRequested;

    private String status;

    @NotBlank(message = "Reason is required")
    private String reason;

    private Long managerId;
    private String managerName;
    private LocalDateTime appliedDate;
    private LocalDateTime approvedDate;
    private String managerComments;
    private String emergencyContact;
}