package com.worklink.hrms.modules.employee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.worklink.hrms.common.entity.Department;
import com.worklink.hrms.modules.employee.entity.Employee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class EmployeeDTO {

    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;

    private String address;

    private Long departmentId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Department department;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String departmentName;

    @NotBlank
    private String position;

    private LocalDate dateOfJoining;

    private Double salary;

    private Employee.EmployeeStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Constructors
    public EmployeeDTO() {}

    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        this.phone = employee.getPhone();
        this.address = employee.getAddress();
        this.departmentId = employee.getDepartmentId();
        this.position = employee.getPosition();
        this.dateOfJoining = employee.getDateOfJoining();
        this.salary = employee.getSalary();
        this.status = employee.getStatus();
        this.createdAt = employee.getCreatedAt();
        this.updatedAt = employee.getUpdatedAt();
    }
    public EmployeeDTO(Long id,
                       String firstName,
                       String lastName,
                       String email,
                       String phone,
                       String address,
                       Long departmentId,
                       String departmentName, // keep this param to receive mapped value
                       String position,
                       LocalDate dateOfJoining,
                       Double salary,
                       Employee.EmployeeStatus status,
                       LocalDateTime createdAt,
                       LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.departmentId = departmentId;
        this.departmentName = departmentName; // set flattened field
        this.position = position;
        this.dateOfJoining = dateOfJoining;
        this.salary = salary;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}