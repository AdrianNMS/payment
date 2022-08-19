package com.bank.payment.controllers.helpers;

import com.bank.payment.handler.ResponseHandler;
import com.bank.payment.models.documents.Payment;
import com.bank.payment.models.utils.Mont;
import com.bank.payment.services.ActiveService;
import com.bank.payment.services.PaymentService;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class PaymentRestControllerUpdate
{
    public static Mono<ResponseEntity<Object>> createPayment(String id, Payment pay, Logger log, PaymentService paymentService)
    {
        pay.setDateRegister(LocalDateTime.now());

        return paymentService.update(id, pay)
                .doOnNext(transaction -> log.info(transaction.toString()))
                .flatMap(transaction -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, transaction)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
    }

    public static Mono<ResponseEntity<Object>> CheckDebt(String id, Payment pay, Logger log, PaymentService paymentService, Mont mont, Payment oldPayment)
    {
        return paymentService.getDebt(pay.getActiveId(), pay.getCreditId())
                .flatMap(debt -> {
                    float currentMont = mont.getMont() - (debt + (pay.getMont() - oldPayment.getMont()));

                    if(currentMont>0)
                        return createPayment(id, pay, log, paymentService);
                    else
                        return Mono.just(ResponseHandler.response("You don't have enough credits", HttpStatus.BAD_REQUEST, null));
                });
    }

    public static Mono<ResponseEntity<Object>> getOldPayment(String id, Payment pay, Logger log, PaymentService paymentService, Mont mont)
    {
        return paymentService.find(id)
                .flatMap(payment -> {
                    if(payment!=null)
                        return CheckDebt(id,pay,log,paymentService,mont,payment);
                    else
                        return Mono.just(ResponseHandler.response("Payment not found", HttpStatus.BAD_REQUEST, null));
                });
    }

    public static Mono<ResponseEntity<Object>> CheckCreditMont(String id, Payment pay, Logger log, PaymentService paymentService, ActiveService activeService)
    {
        return activeService.getMont(pay.getActiveId(),pay.getCreditId())
                .flatMap(responseMont -> {
                    if(responseMont.getData()!=null)
                    {
                        return getOldPayment(id,pay,log,paymentService,responseMont.getData());
                    }
                    else
                        return Mono.just(ResponseHandler.response("Mont Not Found", HttpStatus.BAD_REQUEST, null));
                });
    }

    public static Mono<ResponseEntity<Object>> UpdatePaymentSequence(String id, Payment pay, Logger log, PaymentService paymentService, ActiveService activeService)
    {
        return CheckCreditMont(id, pay, log, paymentService,activeService);
    }
}
