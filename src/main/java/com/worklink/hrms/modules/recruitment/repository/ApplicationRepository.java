package com.worklink.hrms.modules.recruitment.repository;

import com.worklink.hrms.modules.recruitment.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByJobId(Long jobId);

    List<Application> findByStatus(Application.ApplicationStatus status);

    List<Application> findByCandidateEmail(String candidateEmail);
}