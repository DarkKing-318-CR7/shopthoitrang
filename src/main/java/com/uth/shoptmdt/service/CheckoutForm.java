// src/main/java/com/uth/shoptmdt/dto/CheckoutForm.java
package com.uth.shoptmdt.service;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Data
public class CheckoutForm {
    @NotBlank
    private String receiverName;

    @NotBlank
    private String receiverPhone;

    @NotBlank
    private String receiverAddress;

    private String note;

    @NotBlank
    private String paymentMethod; // "COD"
}
