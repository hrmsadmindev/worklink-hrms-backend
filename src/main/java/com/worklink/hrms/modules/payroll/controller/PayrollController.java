package com.worklink.hrms.modules.payroll.controller;

import com.worklink.hrms.common.dto.ApiResponse;
import com.worklink.hrms.modules.payroll.dto.PayrollDTO;
import com.worklink.hrms.modules.payroll.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<PayrollDTO>>> getPayrollByEmployeeId(@PathVariable Long employeeId) {
        try {
            List<PayrollDTO> payrolls = payrollService.getPayrollByEmployeeId(employeeId);
            return ResponseEntity.ok(new ApiResponse<>("Payroll records retrieved successfully", payrolls, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to retrieve payroll records: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<PayrollDTO>> generatePayroll(@RequestParam Long employeeId,
                                                                 @RequestParam Integer month,
                                                                 @RequestParam Integer year) {
        try {
            PayrollDTO generated = payrollService.generatePayroll(employeeId, month, year);
            return ResponseEntity.ok(new ApiResponse<>("Payroll generated successfully", generated, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to generate payroll: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/salary/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<PayrollDTO>>> getSalaryByEmployeeId(@PathVariable Long employeeId) {
        try {
            List<PayrollDTO> salaries = payrollService.getSalaryByEmployeeId(employeeId);
            return ResponseEntity.ok(new ApiResponse<>("Salary records retrieved successfully", salaries, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to retrieve salary records: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/salary")
    public ResponseEntity<ApiResponse<PayrollDTO>> createSalary(@RequestBody PayrollDTO salaryDTO) {
        try {
            PayrollDTO created = payrollService.createSalary(salaryDTO);
            return ResponseEntity.ok(new ApiResponse<>("Salary record created successfully", created, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to create salary record: " + e.getMessage(), null, false));
        }
    }
}