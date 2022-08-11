package com.bank.payment.services;

import com.bank.payment.models.utils.ResponseActive;
import reactor.core.publisher.Mono;

public interface ActiveService {
    Mono<ResponseActive> findByCode(String id);
}
