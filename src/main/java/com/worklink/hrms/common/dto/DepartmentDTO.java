package com.worklink.hrms.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DepartmentDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String location;
    private Double budget;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public DepartmentDTO() {}

    public DepartmentDTO(Long id, String name, String code, String description,
                         String location, Double budget, String status,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.location = location;
        this.budget = budget;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Simple constructor for dropdown (only id and name needed)
    public DepartmentDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
