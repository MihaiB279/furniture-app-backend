package com.back.web.furniture.Controller;

import com.back.web.furniture.Domain.User.Role;
import com.back.web.furniture.Dto.PaypalPaymentDto;
import com.back.web.furniture.Payment.PaymentStatus;
import com.back.web.furniture.Service.ServicePaypal;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class ControllerPaypal {

    private ServicePaypal servicePaypalImpl;

    @Autowired
    public ControllerPaypal(ServicePaypal servicePaypalImpl) {
        this.servicePaypalImpl = servicePaypalImpl;
    }

    @GetMapping("/get")
    public  ResponseEntity<?> getPaymentsUser(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER)) {
                List<PaypalPaymentDto> paymentDtos = servicePaypalImpl.getPaymentsUser(userDetails.getUsername());
                return new ResponseEntity<>(paymentDtos, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createCard")
    public ResponseEntity<?> createCardPayment(@RequestBody PaypalPaymentDto paypalPaymentDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER)) {
                String cancelUrl = "http://localhost:8080/payment/cancel";
                String successUrl = "http://localhost:8080/payment/success";
                Payment payment = servicePaypalImpl.createCardPayment(
                        paypalPaymentDto,
                        userDetails.getUsername(),
                        "EUR",
                        "paypal",
                        "sale",
                        "Purchase",
                        cancelUrl,
                        successUrl
                );

                for (Links links : payment.getLinks()) {
                    if (links.getRel().equals("approval_url")) {
                        return ResponseEntity.ok(links.getHref());
                    }
                }
            } else {
                return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new RedirectView("Error on payment"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/success")
    public ResponseEntity<?> paymentSuccess(@RequestBody Map<String, String> paymentData) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER)) {
                String paymentId = paymentData.get("paymentId");
                String payerId = paymentData.get("payerId");
                Payment payment = servicePaypalImpl.executePayment(paymentId, payerId);
                if (payment.getState().equals("approved")) {
                    PaypalPaymentDto paypalPaymentDto = servicePaypalImpl.saveCardPayment(userDetails.getUsername(), PaymentStatus.APPROVED, ZonedDateTime.parse(payment.getUpdateTime()));
                    return new ResponseEntity<>(paypalPaymentDto, HttpStatus.OK);
                }
                PaypalPaymentDto paypalPaymentDto = servicePaypalImpl.saveCardPayment(userDetails.getUsername(), PaymentStatus.DECLINED, ZonedDateTime.parse(payment.getUpdateTime()));
                return new ResponseEntity<>(paypalPaymentDto, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createCash")
    public ResponseEntity<?> createCashPayment(@RequestBody PaypalPaymentDto paymentData) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER)) {
                PaypalPaymentDto payment = servicePaypalImpl.saveCashPayment(userDetails.getUsername(), paymentData);
                return new ResponseEntity<>(payment, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<?> paymentCancel() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (ControllerUtils.checkRole(userDetails, Role.USER)) {
                PaypalPaymentDto paypalPaymentDto = servicePaypalImpl.cancelPayment(userDetails.getUsername());
                return new ResponseEntity<>(paypalPaymentDto, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
