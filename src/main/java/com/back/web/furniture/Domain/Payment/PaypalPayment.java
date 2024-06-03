package com.back.web.furniture.Domain.Payment;

import com.back.web.furniture.Domain.User.Address;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name="PaymentTable")
public class PaypalPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String username;
    @Column
    private double amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @Column(columnDefinition = "TIMESTAMP")
    private ZonedDateTime dateOfPayment;
    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;
}
