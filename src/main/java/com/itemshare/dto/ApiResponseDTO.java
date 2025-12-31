package com.itemshare.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ApiResponseDTO<T> {

    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

    public ApiResponseDTO() {
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponseDTO(Integer code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(200, "操作成功", data);
    }

    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>(200, message, data);
    }

    public static <T> ApiResponseDTO<T> error(String message) {
        return new ApiResponseDTO<>(500, message, null);
    }

    public static <T> ApiResponseDTO<T> error(Integer code, String message) {
        return new ApiResponseDTO<>(code, message, null);
    }
}
