package com.worklink.hrms.modules.performance.dto;

import com.worklink.hrms.modules.performance.entity.Goal;
import com.worklink.hrms.modules.performance.entity.Review;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PerformanceDTO {

    // Goal fields
    private Long goalId;
    private Long employeeId;
    private String goalTitle;
    private String goalDescription;
    private Goal.GoalStatus goalStatus;
    private Goal.Priority priority;
    private LocalDate targetDate;
    private Integer progressPercentage;

    // Review fields
    private Long reviewId;
    private Long reviewerId;
    private Review.ReviewType reviewType;
    private LocalDate reviewPeriodStart;
    private LocalDate reviewPeriodEnd;
    private Integer overallRating;
    private String strengths;
    private String areasForImprovement;
    private String comments;
    private Review.ReviewStatus reviewStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public PerformanceDTO() {}

    public PerformanceDTO(Goal goal) {
        this.goalId = goal.getId();
        this.employeeId = goal.getEmployeeId();
        this.goalTitle = goal.getTitle();
        this.goalDescription = goal.getDescription();
        this.goalStatus = goal.getStatus();
        this.priority = goal.getPriority();
        this.targetDate = goal.getTargetDate();
        this.progressPercentage = goal.getProgressPercentage();
        this.createdAt = goal.getCreatedAt();
        this.updatedAt = goal.getUpdatedAt();
    }

    public PerformanceDTO(Review review) {
        this.reviewId = review.getId();
        this.employeeId = review.getEmployeeId();
        this.reviewerId = review.getReviewerId();
        this.reviewType = review.getReviewType();
        this.reviewPeriodStart = review.getReviewPeriodStart();
        this.reviewPeriodEnd = review.getReviewPeriodEnd();
        this.overallRating = review.getOverallRating();
        this.strengths = review.getStrengths();
        this.areasForImprovement = review.getAreasForImprovement();
        this.comments = review.getComments();
        this.reviewStatus = review.getStatus();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }

    // Getters and Setters
    public Long getGoalId() { return goalId; }
    public void setGoalId(Long goalId) { this.goalId = goalId; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getGoalTitle() { return goalTitle; }
    public void setGoalTitle(String goalTitle) { this.goalTitle = goalTitle; }

    public String getGoalDescription() { return goalDescription; }
    public void setGoalDescription(String goalDescription) { this.goalDescription = goalDescription; }

    public Goal.GoalStatus getGoalStatus() { return goalStatus; }
    public void setGoalStatus(Goal.GoalStatus goalStatus) { this.goalStatus = goalStatus; }

    public Goal.Priority getPriority() { return priority; }
    public void setPriority(Goal.Priority priority) { this.priority = priority; }

    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }

    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }

    public Long getReviewId() { return reviewId; }
    public void setReviewId(Long reviewId) { this.reviewId = reviewId; }

    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }

    public Review.ReviewType getReviewType() { return reviewType; }
    public void setReviewType(Review.ReviewType reviewType) { this.reviewType = reviewType; }

    public LocalDate getReviewPeriodStart() { return reviewPeriodStart; }
    public void setReviewPeriodStart(LocalDate reviewPeriodStart) { this.reviewPeriodStart = reviewPeriodStart; }

    public LocalDate getReviewPeriodEnd() { return reviewPeriodEnd; }
    public void setReviewPeriodEnd(LocalDate reviewPeriodEnd) { this.reviewPeriodEnd = reviewPeriodEnd; }

    public Integer getOverallRating() { return overallRating; }
    public void setOverallRating(Integer overallRating) { this.overallRating = overallRating; }

    public String getStrengths() { return strengths; }
    public void setStrengths(String strengths) { this.strengths = strengths; }

    public String getAreasForImprovement() { return areasForImprovement; }
    public void setAreasForImprovement(String areasForImprovement) { this.areasForImprovement = areasForImprovement; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public Review.ReviewStatus getReviewStatus() { return reviewStatus; }
    public void setReviewStatus(Review.ReviewStatus reviewStatus) { this.reviewStatus = reviewStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}