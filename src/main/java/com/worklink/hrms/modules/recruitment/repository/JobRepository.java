package com.worklink.hrms.modules.recruitment.repository;

import com.worklink.hrms.modules.recruitment.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByStatus(Job.JobStatus status);

    List<Job> findByDepartment(String department);

    List<Job> findByJobType(Job.JobType jobType);
}