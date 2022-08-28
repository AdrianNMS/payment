package com.bank.payment.services.impl;

import com.bank.payment.models.dao.PaymentDao;
import com.bank.payment.models.documents.MovementRegister;
import com.bank.payment.models.documents.Payment;
import com.bank.payment.services.PaymentService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentImpl implements PaymentService
{
    @Autowired
    private PaymentDao dao;

    @Override
    public Mono<List<Payment>> findAll() {
        return dao.findAll()
                .collectList();
    }

    @Override
    public Mono<Payment> find(String id) {
        return dao.findById(id);
    }

    @Override
    public Mono<Payment> create(Payment pay) {
        return dao.save(pay);
    }

    @Override
    public Mono<Payment> update(String id, Payment pay) {
        return dao.existsById(id).flatMap(check -> {
            if (check)
            {
                pay.setDateUpdate(LocalDateTime.now());
                return dao.save(pay);
            }
            else
                return Mono.empty();
        });
    }

    @Override
    public Mono<Object> delete(String id) {
        return dao.existsById(id).flatMap(check -> {
            if (check)
                return dao.deleteById(id).then(Mono.just(true));
            else
                return Mono.empty();
        });
    }

    @Override
    public Mono<List<Payment>> findByIdClient(String idClient) {
        return findAll()
                .flatMap(payments ->
                        Mono.just(payments.stream()
                            .filter(payment -> payment.getClientId().equals(idClient))
                            .collect(Collectors.toList()))
                );
    }
    @Override
    public Mono<Float> getTotalBalance(String id, String idCredit) {
        LocalDateTime dateNow = LocalDateTime.now();
        return findAll()
                .flatMap(payments ->
                        Mono.just((float)payments.stream()
                                .filter(payment ->
                                        (payment.getActiveId().equals(id) && payment.getCreditId().equals(idCredit))
                                )
                                .mapToDouble(Payment::getMont)
                                .sum()
                        ));
    }

    @Override
    public Mono<Float> getTotalBalanceClient(String idClient)
    {
        return findByIdClient(idClient).flatMap(payments ->
            Mono.just((float)payments
                    .stream()
                    .mapToDouble(Payment::getMont)
                    .sum())
        );
    }
}
