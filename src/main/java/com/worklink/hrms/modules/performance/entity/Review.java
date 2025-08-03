package com.worklink.hrms.modules.performance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long employeeId;

    @Column(nullable = false)
    private Long reviewerId;

    @Enumerated(EnumType.STRING)
    private ReviewType reviewType = ReviewType.ANNUAL;

    private LocalDate reviewPeriodStart;

    private LocalDate reviewPeriodEnd;

    private Integer overallRating;

    @Column(columnDefinition = "TEXT")
    private String strengths;

    @Column(columnDefinition = "TEXT")
    private String areasForImprovement;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.DRAFT;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructors
    public Review() {}

    public Review(Long employeeId, Long reviewerId, ReviewType reviewType) {
        this.employeeId = employeeId;
        this.reviewerId = reviewerId;
        this.reviewType = reviewType;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }

    public ReviewType getReviewType() { return reviewType; }
    public void setReviewType(ReviewType reviewType) { this.reviewType = reviewType; }

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

    public ReviewStatus getStatus() { return status; }
    public void setStatus(ReviewStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum ReviewType {
        ANNUAL, QUARTERLY, MONTHLY, PROBATION
    }

    public enum ReviewStatus {
        DRAFT, SUBMITTED, APPROVED, PUBLISHED
    }
}