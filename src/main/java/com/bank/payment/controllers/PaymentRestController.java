package com.bank.payment.controllers;

import com.bank.payment.handler.ResponseHandler;
import com.bank.payment.models.dao.PaymentDao;
import com.bank.payment.models.documents.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/payment")
public class PaymentRestController
{
    @Autowired
    private PaymentDao dao;
    private static final Logger log = LoggerFactory.getLogger(PaymentRestController.class);

    @GetMapping
    public Mono<ResponseEntity<Object>> findAll()
    {
        log.info("[INI] findAll Payment");
        return dao.findAll()
                .doOnNext(payment -> log.info(payment.toString()))
                .collectList()
                .map(payments -> ResponseHandler.response("Done", HttpStatus.OK, payments))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] findAll Payment"));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> find(@PathVariable String id)
    {
        log.info("[INI] find Payment");
        return dao.findById(id)
                .doOnNext(payment -> log.info(payment.toString()))
                .map(payment -> ResponseHandler.response("Done", HttpStatus.OK, payment))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] find Payment"));
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> create(@Valid @RequestBody Payment pay)
    {
        log.info("[INI] create Payment");
        return dao.save(pay)
                .doOnNext(payment -> log.info(payment.toString()))
                .map(payment -> ResponseHandler.response("Done", HttpStatus.OK, payment)                )
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] create Payment"));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> update(@PathVariable("id") String id, @RequestBody Payment pay)
    {
        log.info("[INI] update Payment");
        return dao.existsById(id).flatMap(check -> {
            if (check)
                return dao.save(pay)
                        .doOnNext(payment -> log.info(payment.toString()))
                        .map(payment -> ResponseHandler.response("Done", HttpStatus.OK, payment)                )
                        .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
            else
                return Mono.just(ResponseHandler.response("Not found", HttpStatus.NOT_FOUND, null));

        }).doFinally(fin -> log.info("[END] update Payment"));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable("id") String id)
    {
        log.info("[INI] delete Payment");
        log.info(id);

        return dao.existsById(id).flatMap(check -> {
            if (check)
                return dao.deleteById(id).then(Mono.just(ResponseHandler.response("Done", HttpStatus.OK, null)));
            else
                return Mono.just(ResponseHandler.response("Not found", HttpStatus.NOT_FOUND, null));
        }).doFinally(fin -> log.info("[END] delete Payment"));
    }
}
