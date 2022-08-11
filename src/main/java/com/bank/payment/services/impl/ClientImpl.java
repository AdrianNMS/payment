package com.bank.payment.services.impl;

import com.bank.payment.models.utils.ResponseClient;
import com.bank.payment.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ClientImpl implements ClientService {

    @Qualifier("getWebClientC")
    @Autowired
    WebClient webClient;

    @Override
    public Mono<ResponseClient> findByCode(String id)
    {
        return webClient.get()
                .uri("/api/client/"+ id)
                .retrieve()
                .bodyToMono(ResponseClient.class);
    }
}
