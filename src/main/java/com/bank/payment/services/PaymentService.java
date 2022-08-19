package com.bank.payment.services;

import com.bank.payment.models.documents.Payment;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PaymentService
{
    Mono<List<Payment>> findAll();
    Mono<Payment> find(String id);
    Mono<Payment> create(Payment pay);
    Mono<Payment> update(String id, Payment pay);
    Mono<Object> delete(String id);
    Mono<List<Payment>> findByIdClient(String id);
    Mono<Float> getBalance(String id, String idCredit);
    Mono<Float> getDebtMonth(String id, String idCredit);

}
