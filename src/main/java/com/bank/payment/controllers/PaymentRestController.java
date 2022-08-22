package com.bank.payment.controllers;

import com.bank.payment.handler.ResponseHandler;
import com.bank.payment.services.ActiveService;
import com.bank.payment.services.ClientService;
import com.bank.payment.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/payment")
public class PaymentRestController
{

    private static final Logger log = LoggerFactory.getLogger(PaymentRestController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ActiveService activeService;

    @Autowired
    private ClientService clientService;

    @GetMapping

    public Mono<ResponseEntity<Object>> findAll()
    {
        log.info("[INI] findAll Payment");
        return paymentService.findAll()
                .flatMap(payments -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, payments)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("No Content", HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] findAll Payment"));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> find(@PathVariable String id)
    {
        log.info("[INI] find Payment");
        return paymentService.find(id)
                .flatMap(payment -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, payment)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("No Content", HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] find Payment"));
    }

    /*@PostMapping
    public Mono<ResponseEntity<Object>> create(@Valid @RequestBody Payment pay)
    {
        log.info("[INI] create payment");

        return PaymentRestControllerCreate.CreatePaymentSequence(pay,log,paymentService,activeService,clientService)
                .doFinally(fin -> log.info("[END] create Payment"));
    }*/

    /*@PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> update(@PathVariable("id") String id, @RequestBody Payment pay)
    {
        log.info("[INI] update Payment");

        return PaymentRestControllerUpdate.UpdatePaymentSequence(id,pay,log,paymentService,activeService)
                .doFinally(fin -> log.info("[END] update Payment"));
    }*/

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable("id") String id)
    {
        log.info("[INI] delete Payment");
        log.info(id);

        return paymentService.delete(id)
                .flatMap(o -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, null)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Error", HttpStatus.NO_CONTENT, null)))
                .doFinally(fin -> log.info("[END] delete Payment"));
    }

    @GetMapping("/clientPayments/{idClient}")
    public Mono<ResponseEntity<Object>> findByIdClient(@PathVariable String idClient)
    {
        log.info("[INI] findByIdClient Payment");
        return paymentService.findByIdClient(idClient)
                .flatMap(payments -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, payments)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("No Content", HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] findByIdClient Payment"));
    }

    @GetMapping("/debt/{id}/{idCredit}")
    public Mono<ResponseEntity<Object>> getDebt(@PathVariable("id") String id, @PathVariable("idCredit") String idCredit)
    {
        log.info("[INI] getDebt Payment");
        log.info(id);

        return paymentService.getTotalBalance(id,idCredit)
                .flatMap(balance -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, balance)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("No Content", HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] getDebt Payment"));
    }

    @GetMapping("/balance/client/{idClient}")
    public Mono<ResponseEntity<Object>> getBalanceClient(@PathVariable("idClient") String idClient)
    {
        log.info("[INI] getBalanceClient Payment");
        log.info(idClient);

        return paymentService.getTotalBalanceClient(idClient)
                .flatMap(balance -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, balance)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("No Content", HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] getBalanceClient Payment"));
    }
}
