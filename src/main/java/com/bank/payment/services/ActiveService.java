package com.bank.payment.services;

import com.bank.payment.models.utils.ResponseActive;
import com.bank.payment.models.utils.ResponseMont;
import reactor.core.publisher.Mono;

public interface ActiveService {
    Mono<ResponseActive> findType(String id);
    Mono<ResponseMont> getMont(String idActive, String idCredit);
    Mono<ResponseMont> getMontTran(String idActive, String idCredit);
}
