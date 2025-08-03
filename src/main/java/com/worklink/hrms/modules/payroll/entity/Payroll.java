package com.worklink.hrms.modules.payroll.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long employeeId;

    @Column(nullable = false)
    private Integer payPeriodMonth;

    @Column(nullable = false)
    private Integer payPeriodYear;

    private Double basicSalary;

    private Double allowances;

    private Double overtime;

    private Double bonuses;

    private Double deductions;

    private Double taxDeductions;

    private Double netSalary;

    private LocalDate payDate;

    @Enumerated(EnumType.STRING)
    private PayrollStatus status = PayrollStatus.PENDING;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructors
    public Payroll() {}

    public Payroll(Long employeeId, Integer payPeriodMonth, Integer payPeriodYear) {
        this.employeeId = employeeId;
        this.payPeriodMonth = payPeriodMonth;
        this.payPeriodYear = payPeriodYear;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public Integer getPayPeriodMonth() { return payPeriodMonth; }
    public void setPayPeriodMonth(Integer payPeriodMonth) { this.payPeriodMonth = payPeriodMonth; }

    public Integer getPayPeriodYear() { return payPeriodYear; }
    public void setPayPeriodYear(Integer payPeriodYear) { this.payPeriodYear = payPeriodYear; }

    public Double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }

    public Double getAllowances() { return allowances; }
    public void setAllowances(Double allowances) { this.allowances = allowances; }

    public Double getOvertime() { return overtime; }
    public void setOvertime(Double overtime) { this.overtime = overtime; }

    public Double getBonuses() { return bonuses; }
    public void setBonuses(Double bonuses) { this.bonuses = bonuses; }

    public Double getDeductions() { return deductions; }
    public void setDeductions(Double deductions) { this.deductions = deductions; }

    public Double getTaxDeductions() { return taxDeductions; }
    public void setTaxDeductions(Double taxDeductions) { this.taxDeductions = taxDeductions; }

    public Double getNetSalary() { return netSalary; }
    public void setNetSalary(Double netSalary) { this.netSalary = netSalary; }

    public LocalDate getPayDate() { return payDate; }
    public void setPayDate(LocalDate payDate) { this.payDate = payDate; }

    public PayrollStatus getStatus() { return status; }
    public void setStatus(PayrollStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum PayrollStatus {
        PENDING, PROCESSED, PAID, CANCELLED
    }
}