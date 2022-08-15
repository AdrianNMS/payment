package com.bank.payment.services;

import com.bank.payment.models.utils.Mont;
import com.bank.payment.models.utils.ResponseMont;
import com.bank.payment.models.utils.ResponseActive;
import reactor.core.publisher.Mono;

public interface ActiveService {
    Mono<ResponseActive> findByCode(String id);
    Mono<ResponseMont> getMont(String idActive, String idCredit);
    Mono<ResponseMont> setMont(String idActive, String idCredit, Mont mont);
}
