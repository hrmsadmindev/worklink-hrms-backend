package com.worklink.hrms.modules.payroll.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "salaries")
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long employeeId;

    @Column(nullable = false)
    private Double basicSalary;

    private Double houseRentAllowance;

    private Double medicalAllowance;

    private Double transportAllowance;

    private Double otherAllowances;

    private Double providentFund;

    private Double professionalTax;

    private Double incomeTax;

    private Double otherDeductions;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    @Enumerated(EnumType.STRING)
    private SalaryStatus status = SalaryStatus.ACTIVE;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructors
    public Salary() {}

    public Salary(Long employeeId, Double basicSalary) {
        this.employeeId = employeeId;
        this.basicSalary = basicSalary;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public Double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }

    public Double getHouseRentAllowance() { return houseRentAllowance; }
    public void setHouseRentAllowance(Double houseRentAllowance) { this.houseRentAllowance = houseRentAllowance; }

    public Double getMedicalAllowance() { return medicalAllowance; }
    public void setMedicalAllowance(Double medicalAllowance) { this.medicalAllowance = medicalAllowance; }

    public Double getTransportAllowance() { return transportAllowance; }
    public void setTransportAllowance(Double transportAllowance) { this.transportAllowance = transportAllowance; }

    public Double getOtherAllowances() { return otherAllowances; }
    public void setOtherAllowances(Double otherAllowances) { this.otherAllowances = otherAllowances; }

    public Double getProvidentFund() { return providentFund; }
    public void setProvidentFund(Double providentFund) { this.providentFund = providentFund; }

    public Double getProfessionalTax() { return professionalTax; }
    public void setProfessionalTax(Double professionalTax) { this.professionalTax = professionalTax; }

    public Double getIncomeTax() { return incomeTax; }
    public void setIncomeTax(Double incomeTax) { this.incomeTax = incomeTax; }

    public Double getOtherDeductions() { return otherDeductions; }
    public void setOtherDeductions(Double otherDeductions) { this.otherDeductions = otherDeductions; }

    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }

    public SalaryStatus getStatus() { return status; }
    public void setStatus(SalaryStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum SalaryStatus {
        ACTIVE, INACTIVE, REVISED
    }
}