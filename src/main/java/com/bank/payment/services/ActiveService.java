package com.bank.payment.services;

import com.bank.payment.models.utils.ResponseActive;
import com.bank.payment.models.utils.ResponseMont;
import reactor.core.publisher.Mono;

public interface ActiveService {
    Mono<ResponseActive> findType(String id);
    Mono<ResponseMont> getDebt(String idActive, String idCredit);
}
