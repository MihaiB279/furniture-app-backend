package com.back.web.furniture.Dto;

import com.back.web.furniture.Domain.Payment.PaymentStatus;
import com.back.web.furniture.Domain.Payment.PaymentType;
import com.back.web.furniture.Domain.User.Address;
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
