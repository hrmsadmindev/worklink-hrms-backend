package com.worklink.hrms.modules.attendance.entity;

import com.worklink.hrms.modules.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_balance",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "leave_type_id", "year"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "allocated_days", nullable = false)
    private Integer allocatedDays = 0;

    @Column(name = "used_days", nullable = false)
    private Integer usedDays = 0;

    @Column(name = "balance")
    private Integer balance = 0;

    @Column(name = "carryover_days")
    private Integer carryoverDays = 0;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Integer getRemainingDays() {
        return (allocatedDays + carryoverDays) - usedDays - balance;
    }

    public Boolean canApplyLeave(Integer daysRequested) {
        return getRemainingDays() >= daysRequested;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}