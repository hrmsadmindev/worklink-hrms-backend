package com.worklink.hrms.modules.employee.dto;

import com.worklink.hrms.modules.employee.entity.Employee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private Long department;

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
        this.department = employee.getDepartmentId();
        this.position = employee.getPosition();
        this.dateOfJoining = employee.getDateOfJoining();
        this.salary = employee.getSalary();
        this.status = employee.getStatus();
        this.createdAt = employee.getCreatedAt();
        this.updatedAt = employee.getUpdatedAt();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Long getDepartment() { return department; }
    public void setDepartment(Long department) { this.department = department; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public LocalDate getDateOfJoining() { return dateOfJoining; }
    public void setDateOfJoining(LocalDate dateOfJoining) { this.dateOfJoining = dateOfJoining; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public Employee.EmployeeStatus getStatus() { return status; }
    public void setStatus(Employee.EmployeeStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}