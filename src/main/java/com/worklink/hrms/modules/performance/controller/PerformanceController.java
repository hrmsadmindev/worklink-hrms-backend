package com.worklink.hrms.modules.performance.controller;

import com.worklink.hrms.common.dto.ApiResponse;
import com.worklink.hrms.modules.performance.dto.PerformanceDTO;
import com.worklink.hrms.modules.performance.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    @GetMapping("/goals/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<PerformanceDTO>>> getGoalsByEmployeeId(@PathVariable Long employeeId) {
        try {
            List<PerformanceDTO> goals = performanceService.getGoalsByEmployeeId(employeeId);
            return ResponseEntity.ok(new ApiResponse<>("Goals retrieved successfully", goals, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to retrieve goals: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/goals")
    public ResponseEntity<ApiResponse<PerformanceDTO>> createGoal(@RequestBody PerformanceDTO goalDTO) {
        try {
            PerformanceDTO created = performanceService.createGoal(goalDTO);
            return ResponseEntity.ok(new ApiResponse<>("Goal created successfully", created, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to create goal: " + e.getMessage(), null, false));
        }
    }

    @PutMapping("/goals/{goalId}/progress")
    public ResponseEntity<ApiResponse<PerformanceDTO>> updateGoalProgress(@PathVariable Long goalId, 
                                                                        @RequestParam Integer progressPercentage) {
        try {
            PerformanceDTO updated = performanceService.updateGoalProgress(goalId, progressPercentage);
            return ResponseEntity.ok(new ApiResponse<>("Goal progress updated successfully", updated, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to update goal progress: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/reviews/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<PerformanceDTO>>> getReviewsByEmployeeId(@PathVariable Long employeeId) {
        try {
            List<PerformanceDTO> reviews = performanceService.getReviewsByEmployeeId(employeeId);
            return ResponseEntity.ok(new ApiResponse<>("Reviews retrieved successfully", reviews, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to retrieve reviews: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/reviews")
    public ResponseEntity<ApiResponse<PerformanceDTO>> createReview(@RequestBody PerformanceDTO reviewDTO) {
        try {
            PerformanceDTO created = performanceService.createReview(reviewDTO);
            return ResponseEntity.ok(new ApiResponse<>("Review created successfully", created, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to create review: " + e.getMessage(), null, false));
        }
    }
}