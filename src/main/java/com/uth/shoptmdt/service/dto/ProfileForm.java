package com.uth.shoptmdt.service.dto;

// src/main/java/.../dto/ProfileForm.java

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileForm {
    @NotBlank(message = "Họ tên không được trống")
    private String fullName;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được trống")
    private String email;

    private String phone;
    private String addressLine;
    private String province;
    private String district;
    private String ward;
}
