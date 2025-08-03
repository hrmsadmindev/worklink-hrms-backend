package com.worklink.hrms.modules.recruitment.dto;

import com.worklink.hrms.modules.recruitment.entity.Application;
import com.worklink.hrms.modules.recruitment.entity.Job;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RecruitmentDTO {

    // Job DTO fields
    private Long jobId;
    private String title;
    private String department;
    private String description;
    private String requirements;
    private String location;
    private Job.JobType jobType;
    private Job.JobStatus jobStatus;
    private Double salaryMin;
    private Double salaryMax;
    private LocalDate applicationDeadline;

    // Application DTO fields
    private Long applicationId;
    private String candidateName;
    private String candidateEmail;
    private String candidatePhone;
    private String coverLetter;
    private Application.ApplicationStatus applicationStatus;
    private LocalDateTime appliedAt;

    // Constructors
    public RecruitmentDTO() {}

    public RecruitmentDTO(Job job) {
        this.jobId = job.getId();
        this.title = job.getTitle();
        this.department = job.getDepartment();
        this.description = job.getDescription();
        this.requirements = job.getRequirements();
        this.location = job.getLocation();
        this.jobType = job.getJobType();
        this.jobStatus = job.getStatus();
        this.salaryMin = job.getSalaryMin();
        this.salaryMax = job.getSalaryMax();
        this.applicationDeadline = job.getApplicationDeadline();
    }

    // Getters and Setters
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Job.JobType getJobType() { return jobType; }
    public void setJobType(Job.JobType jobType) { this.jobType = jobType; }

    public Job.JobStatus getJobStatus() { return jobStatus; }
    public void setJobStatus(Job.JobStatus jobStatus) { this.jobStatus = jobStatus; }

    public Double getSalaryMin() { return salaryMin; }
    public void setSalaryMin(Double salaryMin) { this.salaryMin = salaryMin; }

    public Double getSalaryMax() { return salaryMax; }
    public void setSalaryMax(Double salaryMax) { this.salaryMax = salaryMax; }

    public LocalDate getApplicationDeadline() { return applicationDeadline; }
    public void setApplicationDeadline(LocalDate applicationDeadline) { this.applicationDeadline = applicationDeadline; }

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }

    public String getCandidateEmail() { return candidateEmail; }
    public void setCandidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; }

    public String getCandidatePhone() { return candidatePhone; }
    public void setCandidatePhone(String candidatePhone) { this.candidatePhone = candidatePhone; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public Application.ApplicationStatus getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(Application.ApplicationStatus applicationStatus) { this.applicationStatus = applicationStatus; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}