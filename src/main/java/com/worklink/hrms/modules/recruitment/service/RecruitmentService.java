package com.worklink.hrms.modules.recruitment.service;

import com.worklink.hrms.modules.recruitment.dto.RecruitmentDTO;
import com.worklink.hrms.modules.recruitment.entity.Job;
import com.worklink.hrms.modules.recruitment.entity.Application;
import com.worklink.hrms.modules.recruitment.repository.JobRepository;
import com.worklink.hrms.modules.recruitment.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecruitmentService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    // Job related methods
    public List<RecruitmentDTO> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(RecruitmentDTO::new)
                .collect(Collectors.toList());
    }

    public RecruitmentDTO getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
        return new RecruitmentDTO(job);
    }

    public RecruitmentDTO createJob(RecruitmentDTO jobDTO) {
        Job job = new Job();
        job.setTitle(jobDTO.getTitle());
        job.setDepartment(jobDTO.getDepartment());
        job.setDescription(jobDTO.getDescription());
        job.setRequirements(jobDTO.getRequirements());
        job.setLocation(jobDTO.getLocation());
        job.setJobType(jobDTO.getJobType());
        job.setStatus(jobDTO.getJobStatus());
        job.setSalaryMin(jobDTO.getSalaryMin());
        job.setSalaryMax(jobDTO.getSalaryMax());
        job.setApplicationDeadline(jobDTO.getApplicationDeadline());
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        Job saved = jobRepository.save(job);
        return new RecruitmentDTO(saved);
    }

    // Application related methods
    public List<Application> getApplicationsByJobId(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public Application createApplication(Long jobId, Application application) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        application.setJob(job);
        application.setAppliedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    public Application updateApplicationStatus(Long applicationId, Application.ApplicationStatus status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());

        return applicationRepository.save(application);
    }
}