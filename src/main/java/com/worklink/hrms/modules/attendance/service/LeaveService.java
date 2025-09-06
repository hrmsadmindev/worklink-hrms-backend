package com.worklink.hrms.modules.attendance.service;

import com.worklink.hrms.modules.attendance.dto.LeaveApprovalDTO;
import com.worklink.hrms.modules.attendance.dto.LeaveRequestDTO;
import com.worklink.hrms.modules.attendance.dto.LeaveBalanceDTO;
import com.worklink.hrms.modules.attendance.entity.LeaveRequest;
import com.worklink.hrms.modules.attendance.entity.LeaveBalance;
import com.worklink.hrms.modules.attendance.entity.LeaveType;
import com.worklink.hrms.modules.attendance.repository.LeaveRequestRepository;
import com.worklink.hrms.modules.attendance.repository.LeaveBalanceRepository;
import com.worklink.hrms.modules.attendance.repository.LeaveTypeRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;

    // Existing leave request methods...
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

        // Check leave balance availability
        int currentYear = LocalDate.now().getYear();
        LeaveBalance leaveBalance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndYear(employee.getId(), leaveRequestDTO.getLeaveTypeId(), currentYear)
                .orElse(null);

        if (leaveBalance == null || !leaveBalance.canApplyLeave(leaveRequestDTO.getDaysRequested())) {
            throw new RuntimeException("Insufficient leave balance for this request");
        }

        // Check for overlapping leaves
        List<LeaveRequest> overlappingLeaves = leaveRequestRepository
                .findOverlappingLeaves(employee.getId(), leaveRequestDTO.getStartDate(), leaveRequestDTO.getEndDate());

        if (!overlappingLeaves.isEmpty()) {
            throw new RuntimeException("Leave dates overlap with existing leave request");
        }

        // Get leave type
        LeaveType leaveType = leaveTypeRepository.findById(leaveRequestDTO.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave type not found"));

        // Create leave request
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(leaveType);
        leaveRequest.setStartDate(leaveRequestDTO.getStartDate());
        leaveRequest.setEndDate(leaveRequestDTO.getEndDate());
        leaveRequest.setDaysRequested(leaveRequestDTO.getDaysRequested());
        leaveRequest.setReason(leaveRequestDTO.getReason());
        leaveRequest.setManager(employee.getManager());
        leaveRequest.setStatus(LeaveRequest.LeaveStatus.PENDING);
        leaveRequest.setEmergencyContact(leaveRequestDTO.getEmergencyContact());

        leaveRequest = leaveRequestRepository.save(leaveRequest);

        log.info("Leave request created: Employee={}, Days={}", employee.getId(), leaveRequest.getDaysRequested());

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

        // Update leave balance - deduct used days
        updateLeaveBalance(leaveRequest.getEmployee().getId(),
                leaveRequest.getLeaveType().getId(),
                leaveRequest.getDaysRequested());

        log.info("Leave request approved: ID={}, Employee={}", leaveRequest.getId(), leaveRequest.getEmployee().getId());

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

        log.info("Leave request rejected: ID={}, Employee={}", leaveRequest.getId(), leaveRequest.getEmployee().getId());

        return convertToDTO(leaveRequest);
    }

    // =============== LEAVE BALANCE METHODS ===============

    @Transactional(readOnly = true)
    public List<LeaveBalanceDTO> getEmployeeLeaveBalances(Long employeeId, Integer year) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }

        int targetYear = year != null ? year : LocalDate.now().getYear();

        List<LeaveBalance> balances = leaveBalanceRepository
                .findByEmployeeIdAndYear(employeeId, targetYear);

        return balances.stream()
                .map(this::convertToBalanceDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LeaveBalanceDTO getEmployeeLeaveBalance(Long employeeId, Long leaveTypeId, Integer year) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }

        int targetYear = year != null ? year : LocalDate.now().getYear();

        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, leaveTypeId, targetYear)
                .orElse(null);

        if (balance == null) {
            // Create default balance if not exists
            balance = createDefaultLeaveBalance(employeeId, leaveTypeId, targetYear);
        }

        return convertToBalanceDTO(balance);
    }

    public LeaveBalanceDTO allocateLeaveBalance(Long employeeId, Long leaveTypeId, Integer allocatedDays, Integer year) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave type not found with id: " + leaveTypeId));

        int targetYear = year != null ? year : LocalDate.now().getYear();

        // Check if balance already exists
        LeaveBalance existingBalance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, leaveTypeId, targetYear)
                .orElse(null);

        if (existingBalance != null) {
            // Update existing balance
            existingBalance.setAllocatedDays(allocatedDays);
            existingBalance.setUpdatedAt(LocalDateTime.now());
            existingBalance = leaveBalanceRepository.save(existingBalance);
        } else {
            // Create new balance
            existingBalance = new LeaveBalance();
            existingBalance.setEmployee(employee);
            existingBalance.setLeaveType(leaveType);
            existingBalance.setAllocatedDays(allocatedDays);
            existingBalance.setUsedDays(0);
            existingBalance.setBalance(0);
            existingBalance.setYear(targetYear);
            existingBalance = leaveBalanceRepository.save(existingBalance);
        }

        log.info("Leave balance allocated: Employee={}, LeaveType={}, Days={}, Year={}",
                employeeId, leaveTypeId, allocatedDays, targetYear);

        return convertToBalanceDTO(existingBalance);
    }

    public LeaveBalanceDTO updateCarryoverDays(Long employeeId, Long leaveTypeId, Integer carryoverDays, Integer year) {
        int targetYear = year != null ? year : LocalDate.now().getYear();

        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, leaveTypeId, targetYear)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        balance.setUpdatedAt(LocalDateTime.now());
        balance = leaveBalanceRepository.save(balance);

        log.info("Carryover days updated: Employee={}, LeaveType={}, CarryoverDays={}, Year={}",
                employeeId, leaveTypeId, carryoverDays, targetYear);

        return convertToBalanceDTO(balance);
    }

    @Transactional(readOnly = true)
    public List<LeaveBalanceDTO> getAllEmployeesLeaveBalances(Integer year) {
        int targetYear = year != null ? year : LocalDate.now().getYear();

        List<LeaveBalance> balances = leaveBalanceRepository.findByYear(targetYear);

        return balances.stream()
                .map(this::convertToBalanceDTO)
                .collect(Collectors.toList());
    }

    // Helper method to update leave balance when leave is approved
    private void updateLeaveBalance(Long employeeId, Long leaveTypeId, Integer daysUsed) {
        int currentYear = LocalDate.now().getYear();

        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, leaveTypeId, currentYear)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        balance.setUsedDays(balance.getUsedDays() + daysUsed);
        balance.setUpdatedAt(LocalDateTime.now());
        leaveBalanceRepository.save(balance);

        log.info("Leave balance updated: Employee={}, LeaveType={}, UsedDays={}",
                employeeId, leaveTypeId, balance.getUsedDays());
    }

    // Helper method to create default leave balance
    private LeaveBalance createDefaultLeaveBalance(Long employeeId, Long leaveTypeId, Integer year) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave type not found"));

        LeaveBalance balance = new LeaveBalance();
        balance.setEmployee(employee);
        balance.setLeaveType(leaveType);
        balance.setAllocatedDays(leaveType.getMaxDaysPerYear() != null ? leaveType.getMaxDaysPerYear() : 0);
        balance.setUsedDays(0);
        balance.setBalance(0);
        balance.setYear(year);

        return leaveBalanceRepository.save(balance);
    }

    // Conversion methods
    private LeaveRequestDTO convertToDTO(LeaveRequest leaveRequest) {
        return LeaveRequestDTO.builder()
                .id(leaveRequest.getId())
                .employeeId(leaveRequest.getEmployee().getId())
                .employeeName(leaveRequest.getEmployee().getFirstName() + " " + leaveRequest.getEmployee().getLastName())
                .leaveTypeId(leaveRequest.getLeaveType().getId())
                .leaveTypeName(leaveRequest.getLeaveType().getName())
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .daysRequested(leaveRequest.getDaysRequested())
                .status(leaveRequest.getStatus().name())
                .reason(leaveRequest.getReason())
                .managerId(leaveRequest.getManager() != null ? leaveRequest.getManager().getId() : null)
                .managerName(leaveRequest.getManager() != null ?
                        leaveRequest.getManager().getFirstName() + " " + leaveRequest.getManager().getLastName() : null)
                .appliedDate(leaveRequest.getAppliedDate())
                .approvedDate(leaveRequest.getApprovedDate())
                .managerComments(leaveRequest.getManagerComments())
                .emergencyContact(leaveRequest.getEmergencyContact())
                .build();
    }

    private LeaveBalanceDTO convertToBalanceDTO(LeaveBalance balance) {
        return LeaveBalanceDTO.builder()
                .id(balance.getId())
                .employeeId(balance.getEmployee().getId())
                .employeeName(balance.getEmployee().getFirstName() + " " + balance.getEmployee().getLastName())
                .leaveTypeId(balance.getLeaveType().getId())
                .leaveTypeName(balance.getLeaveType().getName())
                .allocatedDays(balance.getAllocatedDays())
                .usedDays(balance.getUsedDays())
                .balance(balance.getBalance())
                .remainingDays(balance.getRemainingDays())
                .year(balance.getYear())
                .build();
    }
}