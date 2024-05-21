package com.back.web.furniture.Dto;

import com.back.web.furniture.Domain.User.Address;
import com.back.web.furniture.Payment.PaymentStatus;
import com.back.web.furniture.Payment.PaymentType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class PaypalPaymentDto {
    private Double amount;
    private PaymentStatus status;
    private PaymentType paymentType;
    private ZonedDateTime dateOfPayment;
    private Address deliveryAddress;
}
