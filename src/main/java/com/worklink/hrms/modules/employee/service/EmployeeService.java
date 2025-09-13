package com.worklink.hrms.modules.employee.service;

import com.worklink.hrms.modules.employee.dto.EmployeeDTO;
import com.worklink.hrms.modules.employee.entity.Employee;
import com.worklink.hrms.modules.employee.repository.EmployeeRepository;
import com.worklink.hrms.common.repository.DepartmentRepository;
import com.worklink.hrms.modules.user.dto.UserDTO;
import com.worklink.hrms.modules.user.entity.User;
import com.worklink.hrms.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAllActive().stream()
                .map(emp -> new EmployeeDTO(
                        emp.getId(),
                        emp.getFirstName(),
                        emp.getLastName(),
                        emp.getEmail(),
                        emp.getPhone(),
                        emp.getAddress(),
                        emp.getDepartmentId(),
                        emp.getDepartment() != null ? emp.getDepartment().getName() : null,
                        emp.getPosition(),
                        emp.getDateOfJoining(),
                        emp.getSalary(),
                        emp.getStatus(),
                        emp.getCreatedAt(),
                        emp.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return new EmployeeDTO(employee);
    }

    public EmployeeDTO getEmployeeByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findActiveById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with employee ID: " + employeeId));
        return new EmployeeDTO(employee);
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        // Validate department exists
        if (employeeDTO.getDepartmentId() != null) {
            if (!departmentRepository.existsById(employeeDTO.getDepartmentId())) {
                throw new RuntimeException("Department not found with ID: " + employeeDTO.getDepartmentId());
            }
        }
        // Check if email already exists
        if (employeeRepository.findByEmail(employeeDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + employeeDTO.getEmail());
        }

        try {
            Employee employee = convertToEntity(employeeDTO);
            employee.setCreatedAt(LocalDateTime.now());
            employee.setUpdatedAt(LocalDateTime.now());

            Employee savedEmployee = employeeRepository.save(employee);

            // Create corresponding user record for authentication
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(employeeDTO.getEmail());
            // Generate default password
            String defaultPassword = generateDefaultPassword(employeeDTO.getFirstName(), employeeDTO.getLastName());
            userDTO.setPassword(defaultPassword);
            // Set role based on position or department
            User.UserRole role = determineUserRole(employeeDTO.getPosition());
            userDTO.setRole(role);
            userDTO.setStatus(User.UserStatus.ACTIVE);
            userDTO.setEmployeeId(savedEmployee.getId());
            // Create user record
            UserDTO createdUser = userService.createUser(userDTO);

            System.out.println("Employee created with ID: " + savedEmployee.getId() +
                    ", User created with ID: " + createdUser.getId() +
                    ", Default password: " + defaultPassword);

            return new EmployeeDTO(savedEmployee);
        }catch(Exception e) {
            // If user creation fails, the transaction will rollback and employee won't be created either
            e.printStackTrace();
            e.getStackTrace();
            throw new RuntimeException("Failed to create employee and user: " + e.getMessage(), e);
        }
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee existing = employeeRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        // Update fields
        existing.setFirstName(employeeDTO.getFirstName());
        existing.setLastName(employeeDTO.getLastName());
        existing.setPhone(employeeDTO.getPhone());
        existing.setAddress(employeeDTO.getAddress());
        existing.setDepartmentId(employeeDTO.getDepartmentId());
        existing.setPosition(employeeDTO.getPosition());
        existing.setDateOfJoining(employeeDTO.getDateOfJoining());
        existing.setSalary(employeeDTO.getSalary());
        existing.setStatus(employeeDTO.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());

        Employee updated = employeeRepository.save(existing);
        return new EmployeeDTO(updated);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        // Check if employee exists
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }

        // Just set user status to INACTIVE using employee_id
        userService.deactivateStatusByEmployeeId(id);
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
        if(dto.getId() != null){
            employee.setId(dto.getId());
        }
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setAddress(dto.getAddress());
        employee.setDepartmentId(dto.getDepartmentId());
        employee.setPosition(dto.getPosition());
        employee.setDateOfJoining(dto.getDateOfJoining());
        employee.setSalary(dto.getSalary());
        employee.setStatus(dto.getStatus() != null ? dto.getStatus() : Employee.EmployeeStatus.ACTIVE);
        return employee;
    }

    /**
     * Generate a default password for new employees
     * You might want to implement a more sophisticated password generation strategy
     * or send the password via email
     */
    private String generateDefaultPassword(String firstName, String lastName) {
        // Simple default password generation - consider making this more secure
        String cleanFirstName = firstName.toLowerCase();
        // Generate password: FirstnameLastname@123
        return cleanFirstName+"@123";
    }

    /**
     * Determine user role based on position/title
     * You can customize this logic based on your organization's structure
     */
    private User.UserRole determineUserRole(String position) {
        if (position == null) {
            return User.UserRole.EMPLOYEE;
        }
        String pos = position.toLowerCase();
        if (pos.contains("admin") || pos.contains("administrator")) {
            return User.UserRole.ADMIN;
        } else if (pos.contains("hr") || pos.contains("human resource")) {
            return User.UserRole.HR;
        } else if (pos.contains("manager") || pos.contains("lead") || pos.contains("supervisor")
                || pos.contains("director") || pos.contains("head")) {
            return User.UserRole.MANAGER;
        } else {
            return User.UserRole.EMPLOYEE;
        }
    }
}