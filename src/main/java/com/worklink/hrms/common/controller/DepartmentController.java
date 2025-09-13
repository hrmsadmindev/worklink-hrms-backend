package com.worklink.hrms.common.controller;

import com.worklink.hrms.common.dto.ApiResponse;
import com.worklink.hrms.common.entity.Department;
import com.worklink.hrms.common.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Department>>> getAllDepartments() {
        try {
            List<Department> departments = departmentService.getAllActiveDepartments();
            return ResponseEntity.ok(new ApiResponse<>("Departments retrieved successfully", departments, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Failed to retrieve departments: " + e.getMessage(), null, false));
        }
    }
}
