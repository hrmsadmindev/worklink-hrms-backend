package com.worklink.hrms.modules.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiometricDataDTO {
    private Long employeeId;

    @NotBlank(message = "Device ID is required")
    private String deviceId;

    private String biometricType; // FINGERPRINT, FACE, CARD
    private String biometricData; // Base64 encoded biometric data or card number
    private String location;

    @NotBlank(message = "Timestamp is required")
    private String timestamp;

    private String deviceInfo;
    private String ipAddress;
    private Double latitude;
    private Double longitude;
}