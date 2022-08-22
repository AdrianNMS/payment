package com.bank.payment.services.impl;

import com.bank.payment.models.utils.Mont;
import com.bank.payment.models.utils.ResponseDebitCard;
import com.bank.payment.models.utils.ResponseMont;
import com.bank.payment.services.PasiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PasiveImpl implements PasiveService
{
    @Qualifier("getWebClientPasive")
    @Autowired
    WebClient webClient;
    @Override
    public Mono<ResponseDebitCard> payWithDebitCard(String idDebitCard, Mont mont) {
        return webClient.put()
                .uri("/api/active/debitCard/"+ idDebitCard)
                .body(Mono.just(mont), Mont.class)
                .retrieve()
                .bodyToMono(ResponseDebitCard.class);
    }

    @Override
    public Mono<ResponseMont> getMont(String idPasive) {
        return webClient.get()
                .uri("/api/pasive/mont/"+ idPasive)
                .retrieve()
                .bodyToMono(ResponseMont.class);
    }

    @Override
    public Mono<ResponseMont> setMont(String idPasive, Mont mont) {
        return webClient.post()
                .uri("/api/pasive/mont/"+ idPasive)
                .body(Mono.just(mont), Mont.class)
                .retrieve()
                .bodyToMono(ResponseMont.class);
    }
}
