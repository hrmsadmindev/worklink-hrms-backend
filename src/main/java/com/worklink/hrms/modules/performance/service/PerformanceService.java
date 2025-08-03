package com.worklink.hrms.modules.performance.service;

import com.worklink.hrms.modules.performance.dto.PerformanceDTO;
import com.worklink.hrms.modules.performance.entity.Goal;
import com.worklink.hrms.modules.performance.entity.Review;
import com.worklink.hrms.modules.performance.repository.GoalRepository;
import com.worklink.hrms.modules.performance.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerformanceService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // Goal methods
    public List<PerformanceDTO> getGoalsByEmployeeId(Long employeeId) {
        return goalRepository.findByEmployeeId(employeeId).stream()
                .map(PerformanceDTO::new)
                .collect(Collectors.toList());
    }

    public PerformanceDTO createGoal(PerformanceDTO goalDTO) {
        Goal goal = new Goal();
        goal.setEmployeeId(goalDTO.getEmployeeId());
        goal.setTitle(goalDTO.getGoalTitle());
        goal.setDescription(goalDTO.getGoalDescription());
        goal.setPriority(goalDTO.getPriority());
        goal.setTargetDate(goalDTO.getTargetDate());
        goal.setProgressPercentage(goalDTO.getProgressPercentage() != null ? goalDTO.getProgressPercentage() : 0);
        goal.setCreatedAt(LocalDateTime.now());
        goal.setUpdatedAt(LocalDateTime.now());

        Goal saved = goalRepository.save(goal);
        return new PerformanceDTO(saved);
    }

    public PerformanceDTO updateGoalProgress(Long goalId, Integer progressPercentage) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found with id: " + goalId));

        goal.setProgressPercentage(progressPercentage);
        goal.setUpdatedAt(LocalDateTime.now());

        if (progressPercentage >= 100) {
            goal.setStatus(Goal.GoalStatus.COMPLETED);
        }

        Goal updated = goalRepository.save(goal);
        return new PerformanceDTO(updated);
    }

    // Review methods
    public List<PerformanceDTO> getReviewsByEmployeeId(Long employeeId) {
        return reviewRepository.findByEmployeeId(employeeId).stream()
                .map(PerformanceDTO::new)
                .collect(Collectors.toList());
    }

    public PerformanceDTO createReview(PerformanceDTO reviewDTO) {
        Review review = new Review();
        review.setEmployeeId(reviewDTO.getEmployeeId());
        review.setReviewerId(reviewDTO.getReviewerId());
        review.setReviewType(reviewDTO.getReviewType());
        review.setReviewPeriodStart(reviewDTO.getReviewPeriodStart());
        review.setReviewPeriodEnd(reviewDTO.getReviewPeriodEnd());
        review.setOverallRating(reviewDTO.getOverallRating());
        review.setStrengths(reviewDTO.getStrengths());
        review.setAreasForImprovement(reviewDTO.getAreasForImprovement());
        review.setComments(reviewDTO.getComments());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);
        return new PerformanceDTO(saved);
    }
}