package com.worklink.hrms.modules.employee.controller;

import com.worklink.hrms.common.dto.ApiResponse;
import com.worklink.hrms.modules.employee.dto.EmployeeDTO;
import com.worklink.hrms.modules.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAllEmployees() {
        try {
            List<EmployeeDTO> employees = employeeService.getAllEmployees();
            return ResponseEntity.ok(new ApiResponse<>("Employees retrieved successfully", employees, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to retrieve employees: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable Long id) {
        try {
            EmployeeDTO employee = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(new ApiResponse<>("Employee retrieved successfully", employee, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to retrieve employee: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/employee-id/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeByEmployeeId(@PathVariable String employeeId) {
        try {
            EmployeeDTO employee = employeeService.getEmployeeByEmployeeId(employeeId);
            return ResponseEntity.ok(new ApiResponse<>("Employee retrieved successfully", employee, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to retrieve employee: " + e.getMessage(), null, false));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        try {
            EmployeeDTO created = employeeService.createEmployee(employeeDTO);
            return ResponseEntity.ok(new ApiResponse<>("Employee created successfully", created, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to create employee: " + e.getMessage(), null, false));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(@PathVariable Long id, 
                                                                 @Valid @RequestBody EmployeeDTO employeeDTO) {
        try {
            EmployeeDTO updated = employeeService.updateEmployee(id, employeeDTO);
            return ResponseEntity.ok(new ApiResponse<>("Employee updated successfully", updated, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to update employee: " + e.getMessage(), null, false));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok(new ApiResponse<>("Employee deleted successfully", null, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to delete employee: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployeesByDepartment(@PathVariable String department) {
        try {
            List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment(department);
            return ResponseEntity.ok(new ApiResponse<>("Employees retrieved successfully", employees, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to retrieve employees: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> searchEmployees(@RequestParam String keyword) {
        try {
            List<EmployeeDTO> employees = employeeService.searchEmployees(keyword);
            return ResponseEntity.ok(new ApiResponse<>("Search completed successfully", employees, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Search failed: " + e.getMessage(), null, false));
        }
    }
}