package com.worklink.hrms.modules.performance.repository;

import com.worklink.hrms.modules.performance.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByEmployeeId(Long employeeId);

    List<Goal> findByStatus(Goal.GoalStatus status);

    List<Goal> findByEmployeeIdAndStatus(Long employeeId, Goal.GoalStatus status);
}