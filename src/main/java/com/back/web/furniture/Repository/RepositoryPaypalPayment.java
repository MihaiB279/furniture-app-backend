package com.back.web.furniture.Repository;

import com.back.web.furniture.Domain.User.Address;
import com.back.web.furniture.Payment.PaymentStatus;
import com.back.web.furniture.Payment.PaypalPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface RepositoryPaypalPayment extends JpaRepository<PaypalPayment, UUID> {
    public PaypalPayment findByUsernameAndStatus(String username, PaymentStatus status);
    public List<PaypalPayment> findByUsername(String username);
    public boolean existsByDeliveryAddress(Address address);
}
