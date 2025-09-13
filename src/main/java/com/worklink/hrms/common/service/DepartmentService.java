package com.worklink.hrms.common.service;

import com.worklink.hrms.common.entity.Department;
import com.worklink.hrms.common.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllActiveDepartments() {
        // Only return active departments, or all if status filtering not needed
        return departmentRepository.findAllByStatusOrStatusIsNull("ACTIVE");
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}
