package com.worklink.hrms.modules.attendance.entity;

import com.worklink.hrms.modules.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "attendance",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "date"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "clock_in_time")
    private LocalTime clockInTime;

    @Column(name = "clock_out_time")
    private LocalTime clockOutTime;

    @Column(name = "break_duration")
    private Integer breakDuration = 0; // in minutes

    @Column(name = "total_hours")
    private Double totalHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AttendanceStatus status = AttendanceStatus.PRESENT;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum AttendanceStatus {
        PRESENT, ABSENT, PARTIAL, LATE, EARLY_DEPARTURE, WORK_FROM_HOME
    }

    public void calculateTotalHours() {
        if (clockInTime != null && clockOutTime != null) {
            Duration workDuration = Duration.between(clockInTime, clockOutTime);
            double hours = workDuration.toMinutes() / 60.0;
            if (breakDuration != null && breakDuration > 0) {
                hours -= breakDuration / 60.0;
            }
            this.totalHours = Math.max(0, hours);
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        calculateTotalHours();
    }

    @PrePersist
    public void prePersist() {
        if (this.date == null) {
            this.date = LocalDate.now();
        }
        calculateTotalHours();
    }
}