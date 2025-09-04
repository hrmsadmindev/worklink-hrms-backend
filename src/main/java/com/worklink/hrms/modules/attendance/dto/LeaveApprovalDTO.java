package com.worklink.hrms.modules.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApprovalDTO {

    @NotNull(message = "Leave request ID is required")
    private Long leaveRequestId;

    @NotNull(message = "Manager ID is required")
    private Long managerId;

    private String comments;
}