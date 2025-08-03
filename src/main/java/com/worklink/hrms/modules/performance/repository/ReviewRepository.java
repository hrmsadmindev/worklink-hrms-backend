package com.worklink.hrms.modules.performance.repository;

import com.worklink.hrms.modules.performance.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByEmployeeId(Long employeeId);

    List<Review> findByReviewerId(Long reviewerId);

    List<Review> findByStatus(Review.ReviewStatus status);

    List<Review> findByEmployeeIdAndReviewType(Long employeeId, Review.ReviewType reviewType);
}