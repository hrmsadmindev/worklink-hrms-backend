package com.worklink.hrms.modules.attendance.entity;

import com.worklink.hrms.modules.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "leave_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "days_requested", nullable = false)
    private Integer daysRequested;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column(name = "reason", length = 1000)
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @Column(name = "applied_date", nullable = false)
    private LocalDateTime appliedDate = LocalDateTime.now();

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @Column(name = "manager_comments", length = 1000)
    private String managerComments;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum LeaveStatus {
        PENDING, APPROVED, REJECTED, CANCELLED, WITHDRAWN
    }

    public void calculateDaysRequested() {
        if (startDate != null && endDate != null) {
            this.daysRequested = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
    }

    @PrePersist
    public void prePersist() {
        calculateDaysRequested();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        calculateDaysRequested();
    }
}