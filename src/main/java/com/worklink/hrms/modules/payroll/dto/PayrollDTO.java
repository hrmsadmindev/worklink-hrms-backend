package com.worklink.hrms.modules.payroll.dto;

import com.worklink.hrms.modules.payroll.entity.Payroll;
import com.worklink.hrms.modules.payroll.entity.Salary;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PayrollDTO {

    // Payroll fields
    private Long payrollId;
    private Long employeeId;
    private Integer payPeriodMonth;
    private Integer payPeriodYear;
    private Double basicSalary;
    private Double allowances;
    private Double overtime;
    private Double bonuses;
    private Double deductions;
    private Double taxDeductions;
    private Double netSalary;
    private LocalDate payDate;
    private Payroll.PayrollStatus payrollStatus;

    // Salary fields
    private Long salaryId;
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
    private Salary.SalaryStatus salaryStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public PayrollDTO() {}

    public PayrollDTO(Payroll payroll) {
        this.payrollId = payroll.getId();
        this.employeeId = payroll.getEmployeeId();
        this.payPeriodMonth = payroll.getPayPeriodMonth();
        this.payPeriodYear = payroll.getPayPeriodYear();
        this.basicSalary = payroll.getBasicSalary();
        this.allowances = payroll.getAllowances();
        this.overtime = payroll.getOvertime();
        this.bonuses = payroll.getBonuses();
        this.deductions = payroll.getDeductions();
        this.taxDeductions = payroll.getTaxDeductions();
        this.netSalary = payroll.getNetSalary();
        this.payDate = payroll.getPayDate();
        this.payrollStatus = payroll.getStatus();
        this.createdAt = payroll.getCreatedAt();
        this.updatedAt = payroll.getUpdatedAt();
    }

    public PayrollDTO(Salary salary) {
        this.salaryId = salary.getId();
        this.employeeId = salary.getEmployeeId();
        this.basicSalary = salary.getBasicSalary();
        this.houseRentAllowance = salary.getHouseRentAllowance();
        this.medicalAllowance = salary.getMedicalAllowance();
        this.transportAllowance = salary.getTransportAllowance();
        this.otherAllowances = salary.getOtherAllowances();
        this.providentFund = salary.getProvidentFund();
        this.professionalTax = salary.getProfessionalTax();
        this.incomeTax = salary.getIncomeTax();
        this.otherDeductions = salary.getOtherDeductions();
        this.effectiveFrom = salary.getEffectiveFrom();
        this.effectiveTo = salary.getEffectiveTo();
        this.salaryStatus = salary.getStatus();
        this.createdAt = salary.getCreatedAt();
        this.updatedAt = salary.getUpdatedAt();
    }

    // Getters and Setters
    public Long getPayrollId() { return payrollId; }
    public void setPayrollId(Long payrollId) { this.payrollId = payrollId; }

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

    public Payroll.PayrollStatus getPayrollStatus() { return payrollStatus; }
    public void setPayrollStatus(Payroll.PayrollStatus payrollStatus) { this.payrollStatus = payrollStatus; }

    public Long getSalaryId() { return salaryId; }
    public void setSalaryId(Long salaryId) { this.salaryId = salaryId; }

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

    public Salary.SalaryStatus getSalaryStatus() { return salaryStatus; }
    public void setSalaryStatus(Salary.SalaryStatus salaryStatus) { this.salaryStatus = salaryStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}