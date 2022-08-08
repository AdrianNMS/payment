package com.bank.payment.models.dao;

import com.bank.payment.models.documents.Payment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PaymentDao extends ReactiveMongoRepository<Payment, String> {
}
