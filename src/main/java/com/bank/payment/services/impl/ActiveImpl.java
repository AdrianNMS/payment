package com.bank.payment.services.impl;

import com.bank.payment.models.utils.Mont;
import com.bank.payment.models.utils.ResponseMont;
import com.bank.payment.models.utils.ResponseActive;
import com.bank.payment.services.ActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class ActiveImpl implements ActiveService {

    @Qualifier("getWebClientActive")
    @Autowired
    WebClient webClient;

    @Override
    public Mono<ResponseActive> findByCode(String id)
    {
        return webClient.get()
                .uri("/api/active/"+ id)
                .retrieve()
                .bodyToMono(ResponseActive.class);
    }

    @Override
    public Mono<ResponseMont> getMont(String idActive, String idCredit) {
        return webClient.get()
                .uri("/api/active/mont/"+ idActive+"/"+idCredit)
                .retrieve()
                .bodyToMono(ResponseMont.class);
    }

    @Override
    public Mono<ResponseMont> setMont(String idActive, String idCredit, Mont mont) {
        return webClient.post()
                .uri("/api/active/mont/"+ idActive+"/"+idCredit)
                .body(Mono.just(mont), Mont.class)
                .retrieve()
                .bodyToMono(ResponseMont.class);
    }

}
