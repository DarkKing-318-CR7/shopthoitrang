// src/main/java/com/uth/shoptmdt/service/CheckoutForm.java
package com.uth.shoptmdt.service;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckoutForm {
    @NotBlank private String fullName;
    @NotBlank private String phone;
    @NotBlank private String addressLine;

    private String province;
    private String district;
    private String ward;

    private String note;
    @NotBlank private String paymentMethod = "COD";
}
