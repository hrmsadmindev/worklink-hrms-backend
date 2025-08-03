package com.worklink.hrms.common.dto;

public class ApiResponse<T> {

    private String message;
    private T data;
    private boolean success;
    private long timestamp;

    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponse(String message, T data, boolean success) {
        this.message = message;
        this.data = data;
        this.success = success;
        this.timestamp = System.currentTimeMillis();
    }

    // Static factory methods for common responses
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data, true);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(message, null, true);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null, false);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(message, data, false);
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}