package com.bank.payment.services;

import com.bank.payment.models.utils.ResponseActive;
import com.bank.payment.models.utils.ResponseMont;
import reactor.core.publisher.Mono;

public interface ActiveService {
    Mono<ResponseActive> findType(Integer type,String id);
    Mono<ResponseMont> getMont(String idActive, String idCredit);
}
