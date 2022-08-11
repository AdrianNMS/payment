package com.bank.payment.services;

import com.bank.payment.models.utils.ResponseClient;
import reactor.core.publisher.Mono;

public interface ClientService {
    Mono<ResponseClient> findByCode(String id);
}
