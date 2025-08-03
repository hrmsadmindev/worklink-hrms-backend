package com.worklink.hrms.modules.payroll.service;

import com.worklink.hrms.modules.payroll.dto.PayrollDTO;
import com.worklink.hrms.modules.payroll.entity.Payroll;
import com.worklink.hrms.modules.payroll.entity.Salary;
import com.worklink.hrms.modules.payroll.repository.PayrollRepository;
import com.worklink.hrms.modules.payroll.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private SalaryRepository salaryRepository;

    // Payroll methods
    public List<PayrollDTO> getPayrollByEmployeeId(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId).stream()
                .map(PayrollDTO::new)
                .collect(Collectors.toList());
    }

    public PayrollDTO generatePayroll(Long employeeId, Integer month, Integer year) {
        // Check if payroll already exists for the period
        if (payrollRepository.findByEmployeeIdAndPayPeriodMonthAndPayPeriodYear(employeeId, month, year).isPresent()) {
            throw new RuntimeException("Payroll already exists for this period");
        }

        // Get active salary details
        Salary activeSalary = salaryRepository.findActiveByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("No active salary found for employee: " + employeeId));

        // Calculate payroll
        Payroll payroll = new Payroll(employeeId, month, year);
        payroll.setBasicSalary(activeSalary.getBasicSalary());

        Double totalAllowances = (activeSalary.getHouseRentAllowance() != null ? activeSalary.getHouseRentAllowance() : 0) +
                                (activeSalary.getMedicalAllowance() != null ? activeSalary.getMedicalAllowance() : 0) +
                                (activeSalary.getTransportAllowance() != null ? activeSalary.getTransportAllowance() : 0) +
                                (activeSalary.getOtherAllowances() != null ? activeSalary.getOtherAllowances() : 0);

        payroll.setAllowances(totalAllowances);

        Double totalDeductions = (activeSalary.getProvidentFund() != null ? activeSalary.getProvidentFund() : 0) +
                                (activeSalary.getProfessionalTax() != null ? activeSalary.getProfessionalTax() : 0) +
                                (activeSalary.getOtherDeductions() != null ? activeSalary.getOtherDeductions() : 0);

        payroll.setDeductions(totalDeductions);
        payroll.setTaxDeductions(activeSalary.getIncomeTax() != null ? activeSalary.getIncomeTax() : 0);

        Double netSalary = payroll.getBasicSalary() + totalAllowances - totalDeductions - payroll.getTaxDeductions();
        payroll.setNetSalary(netSalary);

        payroll.setCreatedAt(LocalDateTime.now());
        payroll.setUpdatedAt(LocalDateTime.now());

        Payroll saved = payrollRepository.save(payroll);
        return new PayrollDTO(saved);
    }

    // Salary methods
    public List<PayrollDTO> getSalaryByEmployeeId(Long employeeId) {
        return salaryRepository.findByEmployeeId(employeeId).stream()
                .map(PayrollDTO::new)
                .collect(Collectors.toList());
    }

    public PayrollDTO createSalary(PayrollDTO salaryDTO) {
        Salary salary = new Salary();
        salary.setEmployeeId(salaryDTO.getEmployeeId());
        salary.setBasicSalary(salaryDTO.getBasicSalary());
        salary.setHouseRentAllowance(salaryDTO.getHouseRentAllowance());
        salary.setMedicalAllowance(salaryDTO.getMedicalAllowance());
        salary.setTransportAllowance(salaryDTO.getTransportAllowance());
        salary.setOtherAllowances(salaryDTO.getOtherAllowances());
        salary.setProvidentFund(salaryDTO.getProvidentFund());
        salary.setProfessionalTax(salaryDTO.getProfessionalTax());
        salary.setIncomeTax(salaryDTO.getIncomeTax());
        salary.setOtherDeductions(salaryDTO.getOtherDeductions());
        salary.setEffectiveFrom(salaryDTO.getEffectiveFrom());
        salary.setCreatedAt(LocalDateTime.now());
        salary.setUpdatedAt(LocalDateTime.now());

        Salary saved = salaryRepository.save(salary);
        return new PayrollDTO(saved);
    }
}