package com.back.web.furniture.Service;

import com.back.web.furniture.Dto.PaypalPaymentDto;
import com.back.web.furniture.Payment.PaymentStatus;
import com.back.web.furniture.Payment.PaypalPayment;
import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;

import java.time.ZonedDateTime;
import java.util.List;

public interface ServicePaypal {
    public Payment createCardPayment(PaypalPaymentDto paypalPaymentDto,String username, String currency, String method, String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException;

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
    public PaypalPaymentDto saveCardPayment(String username, PaymentStatus status, ZonedDateTime date);
    public PaypalPaymentDto saveCashPayment(String username, PaypalPaymentDto paypalPaymentDto);
    public PaypalPaymentDto cancelPayment(String username);

    public List<PaypalPaymentDto> getPaymentsUser(String username);
}
