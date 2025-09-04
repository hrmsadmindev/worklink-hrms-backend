package com.worklink.hrms.modules.employee.service;

import com.worklink.hrms.modules.employee.dto.EmployeeDTO;
import com.worklink.hrms.modules.employee.entity.Employee;
import com.worklink.hrms.modules.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return new EmployeeDTO(employee);
    }

    public EmployeeDTO getEmployeeByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with employee ID: " + employeeId));
        return new EmployeeDTO(employee);
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        // Check if employee ID already exists
        if (employeeRepository.findById(employeeDTO.getEmployeeId()).isPresent()) {
            throw new RuntimeException("Employee ID already exists: " + employeeDTO.getEmployeeId());
        }

        // Check if email already exists
        if (employeeRepository.findByEmail(employeeDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + employeeDTO.getEmail());
        }

        Employee employee = convertToEntity(employeeDTO);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        Employee saved = employeeRepository.save(employee);
        return new EmployeeDTO(saved);
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        // Update fields
        existing.setFirstName(employeeDTO.getFirstName());
        existing.setLastName(employeeDTO.getLastName());
        existing.setPhone(employeeDTO.getPhone());
        existing.setAddress(employeeDTO.getAddress());
        existing.setDepartmentId(employeeDTO.getDepartment());
        existing.setPosition(employeeDTO.getPosition());
        existing.setDateOfJoining(employeeDTO.getDateOfJoining());
        existing.setSalary(employeeDTO.getSalary());
        existing.setStatus(employeeDTO.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());

        Employee updated = employeeRepository.save(existing);
        return new EmployeeDTO(updated);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    public List<EmployeeDTO> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> searchEmployees(String keyword) {
        return employeeRepository.searchEmployees(keyword).stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
    }

    private Employee convertToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setId(dto.getEmployeeId());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setAddress(dto.getAddress());
        employee.setDepartmentId(dto.getDepartment());
        employee.setPosition(dto.getPosition());
        employee.setDateOfJoining(dto.getDateOfJoining());
        employee.setSalary(dto.getSalary());
        employee.setStatus(dto.getStatus() != null ? dto.getStatus() : Employee.EmployeeStatus.ACTIVE);
        return employee;
    }
}