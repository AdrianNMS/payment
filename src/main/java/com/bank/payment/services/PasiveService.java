package com.bank.payment.services;

import com.bank.payment.models.utils.Mont;
import com.bank.payment.models.utils.ResponseMont;
import com.bank.payment.models.utils.ResponsePasive;
import reactor.core.publisher.Mono;

public interface PasiveService
{
    Mono<ResponsePasive> payWithDebitCard(String idCreditCard, Mont mont);

    Mono<ResponseMont> getMont(String idPasive);
    Mono<ResponseMont> setMont(String idPasive, Mont mont);
}
