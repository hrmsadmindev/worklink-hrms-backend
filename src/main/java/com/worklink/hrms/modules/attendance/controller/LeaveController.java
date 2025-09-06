package com.worklink.hrms.modules.attendance.controller;

import com.worklink.hrms.common.dto.ApiResponse;
import com.worklink.hrms.modules.attendance.dto.LeaveApprovalDTO;
import com.worklink.hrms.modules.attendance.dto.LeaveRequestDTO;
import com.worklink.hrms.modules.attendance.dto.LeaveBalanceDTO;
import com.worklink.hrms.modules.attendance.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leave")
@RequiredArgsConstructor
@Slf4j
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> createLeaveRequest(@Valid @RequestBody LeaveRequestDTO leaveRequestDTO) {
        try {
            log.info("Leave request received for employee: {}", leaveRequestDTO.getEmployeeId());
            LeaveRequestDTO created = leaveService.createLeaveRequest(leaveRequestDTO);
            return ResponseEntity.ok(new ApiResponse<>("Leave request created successfully", created, true));
        } catch (Exception e) {
            log.error("Failed to create leave request", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Failed to create leave request: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<Page<LeaveRequestDTO>>> getEmployeeLeaveRequests(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "appliedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<LeaveRequestDTO> leaveRequests = leaveService.getEmployeeLeaveRequests(employeeId, pageable);
            return ResponseEntity.ok(new ApiResponse<>("Leave requests retrieved successfully", leaveRequests, true));
        } catch (Exception e) {
            log.error("Failed to retrieve leave requests", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Failed to retrieve leave requests: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/pending-approvals/{managerId}")
    public ResponseEntity<ApiResponse<Page<LeaveRequestDTO>>> getPendingApprovals(
            @PathVariable Long managerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "appliedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<LeaveRequestDTO> pendingLeaves = leaveService.getPendingApprovals(managerId, pageable);
            return ResponseEntity.ok(new ApiResponse<>("Pending approvals retrieved successfully", pendingLeaves, true));
        } catch (Exception e) {
            log.error("Failed to retrieve pending approvals", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Failed to retrieve pending approvals: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/approve")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> approveLeave(@Valid @RequestBody LeaveApprovalDTO approvalDTO) {
        try {
            log.info("Leave approval request for leave ID: {}", approvalDTO.getLeaveRequestId());
            LeaveRequestDTO approved = leaveService.approveLeave(approvalDTO);
            return ResponseEntity.ok(new ApiResponse<>("Leave request approved successfully", approved, true));
        } catch (Exception e) {
            log.error("Failed to approve leave request", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Failed to approve leave request: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/reject")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> rejectLeave(@Valid @RequestBody LeaveApprovalDTO rejectionDTO) {
        try {
            log.info("Leave rejection request for leave ID: {}", rejectionDTO.getLeaveRequestId());
            LeaveRequestDTO rejected = leaveService.rejectLeave(rejectionDTO);
            return ResponseEntity.ok(new ApiResponse<>("Leave request rejected successfully", rejected, true));
        } catch (Exception e) {
            log.error("Failed to reject leave request", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Failed to reject leave request: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/balance/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<LeaveBalanceDTO>>> getEmployeeLeaveBalances(
            @PathVariable Long employeeId,
            @RequestParam(required = false) Integer year) {
        try {
            log.info("Getting leave balances for employee: {}, year: {}", employeeId, year);
            List<LeaveBalanceDTO> balances = leaveService.getEmployeeLeaveBalances(employeeId, year);
            return ResponseEntity.ok(new ApiResponse<>("Leave balances retrieved successfully", balances, true));
        } catch (Exception e) {
            log.error("Failed to retrieve leave balances", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Failed to retrieve leave balances: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/balance/employee/{employeeId}/leave-type/{leaveTypeId}")
    public ResponseEntity<ApiResponse<LeaveBalanceDTO>> getEmployeeLeaveBalance(
            @PathVariable Long employeeId,
            @PathVariable Long leaveTypeId,
            @RequestParam(required = false) Integer year) {
        try {
            log.info("Getting leave balance for employee: {}, leave type: {}, year: {}", employeeId, leaveTypeId, year);
            LeaveBalanceDTO balance = leaveService.getEmployeeLeaveBalance(employeeId, leaveTypeId, year);
            return ResponseEntity.ok(new ApiResponse<>("Leave balance retrieved successfully", balance, true));
        } catch (Exception e) {
            log.error("Failed to retrieve leave balance", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Failed to retrieve leave balance: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/balance/allocate")
    public ResponseEntity<ApiResponse<LeaveBalanceDTO>> allocateLeaveBalance(
            @RequestParam Long employeeId,
            @RequestParam Long leaveTypeId,
            @RequestParam Integer allocatedDays,
            @RequestParam(required = false) Integer year) {
        try {
            log.info("Allocating leave balance: Employee={}, LeaveType={}, Days={}, Year={}",
                    employeeId, leaveTypeId, allocatedDays, year);
            LeaveBalanceDTO balance = leaveService.allocateLeaveBalance(employeeId, leaveTypeId, allocatedDays, year);
            return ResponseEntity.ok(new ApiResponse<>("Leave balance allocated successfully", balance, true));
        } catch (Exception e) {
            log.error("Failed to allocate leave balance", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Failed to allocate leave balance: " + e.getMessage(), null, false));
        }
    }

    @PutMapping("/balance/carryover")
    public ResponseEntity<ApiResponse<LeaveBalanceDTO>> updateCarryoverDays(
            @RequestParam Long employeeId,
            @RequestParam Long leaveTypeId,
            @RequestParam Integer carryoverDays,
            @RequestParam(required = false) Integer year) {
        try {
            log.info("Updating carryover days: Employee={}, LeaveType={}, CarryoverDays={}, Year={}",
                    employeeId, leaveTypeId, carryoverDays, year);
            LeaveBalanceDTO balance = leaveService.updateCarryoverDays(employeeId, leaveTypeId, carryoverDays, year);
            return ResponseEntity.ok(new ApiResponse<>("Carryover days updated successfully", balance, true));
        } catch (Exception e) {
            log.error("Failed to update carryover days", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Failed to update carryover days: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/balance/all")
    public ResponseEntity<ApiResponse<List<LeaveBalanceDTO>>> getAllEmployeesLeaveBalances(
            @RequestParam(required = false) Integer year) {
        try {
            log.info("Getting all employees leave balances for year: {}", year);
            List<LeaveBalanceDTO> balances = leaveService.getAllEmployeesLeaveBalances(year);
            return ResponseEntity.ok(new ApiResponse<>("All employees leave balances retrieved successfully", balances, true));
        } catch (Exception e) {
            log.error("Failed to retrieve all employees leave balances", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Failed to retrieve leave balances: " + e.getMessage(), null, false));
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        log.error("Leave API error", e);
        Map<String, Object> error = Map.of(
                "error", e.getMessage(),
                "timestamp", java.time.Instant.now().toString()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}