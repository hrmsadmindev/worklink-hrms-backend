package com.worklink.hrms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main Spring Boot Application Class for HRMS
 * Complete HR Management System with all modules:
 * - Employee Management
 * - Recruitment System
 * - Performance Management
 * - Payroll Management
 * - User Management with Role-based Access Control
 */
@SpringBootApplication
@EnableJpaAuditing
public class HrmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(HrmsApplication.class, args);
        System.out.println("HRMS Application Started Successfully!");
//        System.out.println("Access H2 Console: http://localhost:8080/api/h2-console");
//        System.out.println("Admin Login: admin@company.com / admin123");
//        System.out.println("Manager Login: manager@company.com / manager123");
//        System.out.println("Employee Login: employee@company.com / employee123");
    }
}