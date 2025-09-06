package com.worklink.hrms.modules.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private Long leaveTypeId;
    private String leaveTypeName;
    private Integer allocatedDays;
    private Integer usedDays;
    private Integer balance;
    private Integer remainingDays;
    private Integer year;

    public Double getUtilizationPercentage() {
        if (allocatedDays != null && allocatedDays > 0) {
            return (usedDays != null ? usedDays : 0) * 100.0 / allocatedDays;
        }
        return 0.0;
    }
}