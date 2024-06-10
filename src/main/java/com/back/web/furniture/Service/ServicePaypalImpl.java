package com.back.web.furniture.Service;

import com.back.web.furniture.Domain.Payment.PaymentStatus;
import com.back.web.furniture.Domain.Payment.PaymentType;
import com.back.web.furniture.Domain.Payment.PaypalPayment;
import com.back.web.furniture.Domain.User.Address;
import com.back.web.furniture.Dto.PaypalPaymentDto;
import com.back.web.furniture.Repository.RepositoryAddress;
import com.back.web.furniture.Repository.RepositoryPaypalPayment;
import com.back.web.furniture.Repository.RepositoryShoppingCart;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.back.web.furniture.Service.ServiceUtils.setAddressDetails;

@Service
@RequiredArgsConstructor
public class ServicePaypalImpl implements ServicePaypal{
    private final APIContext apiContext;
    private final RepositoryPaypalPayment repositoryPaypalPayment;
    private final RepositoryAddress repositoryAddress;
    private final RepositoryShoppingCart repositoryShoppingCart;
    private final ModelMapper modelMapper;
    @Transactional
    public Payment createCardPayment(
            PaypalPaymentDto paypalPaymentDto,
            String username,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", paypalPaymentDto.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);

        PaypalPayment paypalPayment = new PaypalPayment();
        paypalPayment.setAmount(Double.valueOf(payment.getTransactions().get(0).getAmount().getTotal()));
        paypalPayment.setStatus(PaymentStatus.IN_PROCESS);
        paypalPayment.setUsername(username);
        paypalPayment.setPaymentType(PaymentType.CARD);
        Address address = repositoryAddress.findByApartmentNumberAndBuildingNumberAndNumberAndStairsAndStreetAndCityAndCounty(
                paypalPaymentDto.getDeliveryAddress().getApartmentNumber(),
                paypalPaymentDto.getDeliveryAddress().getBuildingNumber(),
                paypalPaymentDto.getDeliveryAddress().getNumber(),
                paypalPaymentDto.getDeliveryAddress().getStairs(),
                paypalPaymentDto.getDeliveryAddress().getStreet(),
                paypalPaymentDto.getDeliveryAddress().getCity(),
                paypalPaymentDto.getDeliveryAddress().getCounty()
        );
        if(address == null) {
            address = new Address();
            setAddressDetails(address, modelMapper.map(paypalPaymentDto.getDeliveryAddress(), Address.class));
            repositoryAddress.save(address);
        }

        paypalPayment.setDeliveryAddress(address);
        paypalPayment.setDateOfPayment(ZonedDateTime.now());
        repositoryPaypalPayment.save(paypalPayment);

        return payment.create(apiContext);
    }

    public Payment executePayment(
            String paymentId,
            String payerId
    ) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }

    @Transactional
    public PaypalPaymentDto saveCardPayment(String username, PaymentStatus status, ZonedDateTime date){
        PaypalPayment paypalPayment = repositoryPaypalPayment.findByUsernameAndStatus(username, PaymentStatus.IN_PROCESS);
        paypalPayment.setStatus(status);
        paypalPayment.setDateOfPayment(date);
        repositoryPaypalPayment.save(paypalPayment);
        repositoryShoppingCart.deleteAllByUsername(username);
        return modelMapper.map(paypalPayment, PaypalPaymentDto.class);
    }

    @Transactional
    @Override
    public PaypalPaymentDto saveCashPayment(String username, PaypalPaymentDto paypalPaymentDto) {
        PaypalPayment paypalPayment = modelMapper.map(paypalPaymentDto, PaypalPayment.class);
        paypalPayment.setStatus(PaymentStatus.APPROVED);
        paypalPayment.setPaymentType(PaymentType.CASH);
        paypalPayment.setDateOfPayment(ZonedDateTime.now());
        paypalPayment.setUsername(username);
        Address address = repositoryAddress.findByApartmentNumberAndBuildingNumberAndNumberAndStairsAndStreetAndCityAndCounty(
                paypalPaymentDto.getDeliveryAddress().getApartmentNumber(),
                paypalPaymentDto.getDeliveryAddress().getBuildingNumber(),
                paypalPaymentDto.getDeliveryAddress().getNumber(),
                paypalPaymentDto.getDeliveryAddress().getStairs(),
                paypalPaymentDto.getDeliveryAddress().getStreet(),
                paypalPaymentDto.getDeliveryAddress().getCity(),
                paypalPaymentDto.getDeliveryAddress().getCounty()
        );
        if(address == null) {
            address = new Address();
            setAddressDetails(address, modelMapper.map(paypalPaymentDto.getDeliveryAddress(), Address.class));
            repositoryAddress.save(address);
        }
        paypalPayment.setDeliveryAddress(address);
        repositoryPaypalPayment.save(paypalPayment);
        repositoryShoppingCart.deleteAllByUsername(username);
        return modelMapper.map(paypalPayment, PaypalPaymentDto.class);
    }

    @Override
    public PaypalPaymentDto cancelPayment(String username) {
        PaypalPayment paypalPayment = repositoryPaypalPayment.findByUsernameAndStatus(username, PaymentStatus.IN_PROCESS);
        paypalPayment.setStatus(PaymentStatus.CANCELED);
        paypalPayment.setDateOfPayment(ZonedDateTime.now());
        repositoryPaypalPayment.save(paypalPayment);
        return modelMapper.map(paypalPayment, PaypalPaymentDto.class);
    }

    @Override
    public List<PaypalPaymentDto> getPaymentsUser(String username) {
        List<PaypalPayment> payments = repositoryPaypalPayment.findByUsername(username);
        return payments.stream()
                .map(payment -> modelMapper.map(payment, PaypalPaymentDto.class))
                .toList();
    }
}
