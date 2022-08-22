package com.bank.payment.services;

import com.bank.payment.models.utils.Mont;
import com.bank.payment.models.utils.ResponseDebitCard;
import com.bank.payment.models.utils.ResponseMont;
import reactor.core.publisher.Mono;

public interface PasiveService
{
    Mono<ResponseDebitCard> payWithDebitCard(String idCreditCard, Mont mont);

    Mono<ResponseMont> getMont(String idPasive);
    Mono<ResponseMont> setMont(String idPasive, Mont mont);
}
