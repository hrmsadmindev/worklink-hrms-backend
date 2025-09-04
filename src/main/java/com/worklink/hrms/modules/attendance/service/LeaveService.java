package com.worklink.hrms.modules.attendance.service;

import com.worklink.hrms.modules.attendance.dto.LeaveApprovalDTO;
import com.worklink.hrms.modules.attendance.dto.LeaveRequestDTO;
import com.worklink.hrms.modules.attendance.entity.LeaveRequest;
import com.worklink.hrms.modules.attendance.repository.LeaveRequestRepository;
import com.worklink.hrms.modules.employee.entity.Employee;
import com.worklink.hrms.modules.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveRequestDTO createLeaveRequest(LeaveRequestDTO leaveRequestDTO) {
        // Validate employee
        Employee employee = employeeRepository.findById(leaveRequestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + leaveRequestDTO.getEmployeeId()));

        // Validate dates
        if (leaveRequestDTO.getStartDate().isAfter(leaveRequestDTO.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }

        if (leaveRequestDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot apply for leaves in the past");
        }

        // Check for overlapping leaves
        List<LeaveRequest> overlappingLeaves = leaveRequestRepository
                .findOverlappingLeaves(employee.getId(), leaveRequestDTO.getStartDate(), leaveRequestDTO.getEndDate());

        if (!overlappingLeaves.isEmpty()) {
            throw new RuntimeException("Leave dates overlap with existing leave request");
        }

        // Create leave request
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setStartDate(leaveRequestDTO.getStartDate());
        leaveRequest.setEndDate(leaveRequestDTO.getEndDate());
        leaveRequest.setDaysRequested(leaveRequestDTO.getDaysRequested());
        leaveRequest.setReason(leaveRequestDTO.getReason());
        leaveRequest.setManager(employee.getManager());
        leaveRequest.setStatus(LeaveRequest.LeaveStatus.PENDING);

        leaveRequest = leaveRequestRepository.save(leaveRequest);

        log.info("Leave request created: Employee={}, Days={}",
                employee.getId(), leaveRequest.getDaysRequested());

        return convertToDTO(leaveRequest);
    }

    @Transactional(readOnly = true)
    public Page<LeaveRequestDTO> getEmployeeLeaveRequests(Long employeeId, Pageable pageable) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }

        Page<LeaveRequest> leavePage = leaveRequestRepository
                .findByEmployeeIdOrderByAppliedDateDesc(employeeId, pageable);

        return leavePage.map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Page<LeaveRequestDTO> getPendingApprovals(Long managerId, Pageable pageable) {
        if (!employeeRepository.existsById(managerId)) {
            throw new RuntimeException("Manager not found with id: " + managerId);
        }

        Page<LeaveRequest> pendingLeaves = leaveRequestRepository
                .findByManagerIdAndStatusOrderByAppliedDateDesc(managerId,
                        LeaveRequest.LeaveStatus.PENDING,
                        pageable);

        return pendingLeaves.map(this::convertToDTO);
    }

    public LeaveRequestDTO approveLeave(LeaveApprovalDTO approvalDTO) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(approvalDTO.getLeaveRequestId())
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + approvalDTO.getLeaveRequestId()));

        if (leaveRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            throw new RuntimeException("Leave request is not in pending status");
        }

        // Update leave request
        leaveRequest.setStatus(LeaveRequest.LeaveStatus.APPROVED);
        leaveRequest.setApprovedDate(LocalDateTime.now());
        leaveRequest.setManagerComments(approvalDTO.getComments());

        leaveRequest = leaveRequestRepository.save(leaveRequest);

        log.info("Leave request approved: ID={}, Employee={}",
                leaveRequest.getId(), leaveRequest.getEmployee().getId());

        return convertToDTO(leaveRequest);
    }

    public LeaveRequestDTO rejectLeave(LeaveApprovalDTO rejectionDTO) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(rejectionDTO.getLeaveRequestId())
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + rejectionDTO.getLeaveRequestId()));

        if (leaveRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            throw new RuntimeException("Leave request is not in pending status");
        }

        // Update leave request
        leaveRequest.setStatus(LeaveRequest.LeaveStatus.REJECTED);
        leaveRequest.setApprovedDate(LocalDateTime.now());
        leaveRequest.setManagerComments(rejectionDTO.getComments());

        leaveRequest = leaveRequestRepository.save(leaveRequest);

        log.info("Leave request rejected: ID={}, Employee={}",
                leaveRequest.getId(), leaveRequest.getEmployee().getId());

        return convertToDTO(leaveRequest);
    }

    private LeaveRequestDTO convertToDTO(LeaveRequest leaveRequest) {
        return LeaveRequestDTO.builder()
                .id(leaveRequest.getId())
                .employeeId(leaveRequest.getEmployee().getId())
                .employeeName(leaveRequest.getEmployee().getFirstName()
                                + leaveRequest.getEmployee().getLastName())
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .daysRequested(leaveRequest.getDaysRequested())
                .status(leaveRequest.getStatus().name())
                .reason(leaveRequest.getReason())
                .managerId(leaveRequest.getManager() != null ? leaveRequest.getManager().getId() : null)
                .managerName(leaveRequest.getManager() != null ?
                        leaveRequest.getManager().getFirstName() + leaveRequest.getEmployee().getLastName(): null)
                .appliedDate(leaveRequest.getAppliedDate())
                .approvedDate(leaveRequest.getApprovedDate())
                .managerComments(leaveRequest.getManagerComments())
                .build();
    }
}