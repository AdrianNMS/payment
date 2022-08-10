package com.bank.payment.services;

import com.bank.payment.models.utils.ResponseService;
import reactor.core.publisher.Mono;


public interface ActiveService {

    Mono<ResponseService> findByCode(String id);
}
