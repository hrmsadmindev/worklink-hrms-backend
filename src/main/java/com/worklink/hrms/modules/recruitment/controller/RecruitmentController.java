package com.worklink.hrms.modules.recruitment.controller;

import com.worklink.hrms.common.dto.ApiResponse;
import com.worklink.hrms.modules.recruitment.dto.RecruitmentDTO;
import com.worklink.hrms.modules.recruitment.entity.Application;
import com.worklink.hrms.modules.recruitment.service.RecruitmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruitment")
public class RecruitmentController {

    @Autowired
    private RecruitmentService recruitmentService;

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<List<RecruitmentDTO>>> getAllJobs() {
        try {
            List<RecruitmentDTO> jobs = recruitmentService.getAllJobs();
            return ResponseEntity.ok(new ApiResponse<>("Jobs retrieved successfully", jobs, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to retrieve jobs: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<RecruitmentDTO>> getJobById(@PathVariable Long id) {
        try {
            RecruitmentDTO job = recruitmentService.getJobById(id);
            return ResponseEntity.ok(new ApiResponse<>("Job retrieved successfully", job, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to retrieve job: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/jobs")
    public ResponseEntity<ApiResponse<RecruitmentDTO>> createJob(@RequestBody RecruitmentDTO jobDTO) {
        try {
            RecruitmentDTO created = recruitmentService.createJob(jobDTO);
            return ResponseEntity.ok(new ApiResponse<>("Job created successfully", created, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to create job: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/jobs/{jobId}/applications")
    public ResponseEntity<ApiResponse<List<Application>>> getApplicationsByJobId(@PathVariable Long jobId) {
        try {
            List<Application> applications = recruitmentService.getApplicationsByJobId(jobId);
            return ResponseEntity.ok(new ApiResponse<>("Applications retrieved successfully", applications, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to retrieve applications: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/jobs/{jobId}/applications")
    public ResponseEntity<ApiResponse<Application>> createApplication(@PathVariable Long jobId, 
                                                                    @RequestBody Application application) {
        try {
            Application created = recruitmentService.createApplication(jobId, application);
            return ResponseEntity.ok(new ApiResponse<>("Application submitted successfully", created, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to submit application: " + e.getMessage(), null, false));
        }
    }
}