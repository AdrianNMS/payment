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

public class PaymentRestControllerCreate
{
    public static Mono<ResponseEntity<Object>> createPayment(Payment pay, Logger log, PaymentService paymentService)
    {
        pay.setDateRegister(LocalDateTime.now());

        return paymentService.create(pay)
                .doOnNext(transaction -> log.info(transaction.toString()))
                .flatMap(transaction -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, transaction)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
    }

    public static Mono<ResponseEntity<Object>> CheckDebt(Payment pay, Logger log, PaymentService paymentService, Mont mont)
    {
        return paymentService.getDebtMonth(pay.getActiveId(), pay.getCreditId())
                .flatMap(debt -> {
                    float currentMont = mont.getMont() - (debt + pay.getMont());

                    if(currentMont>0)
                        return createPayment(pay, log, paymentService);
                    else
                        return Mono.just(ResponseHandler.response("You don't have enough credits", HttpStatus.BAD_REQUEST, null));
                });
    }

    public static Mono<ResponseEntity<Object>> CheckCreditMont(Payment pay, Logger log, PaymentService paymentService, ActiveService activeService)
    {
        return activeService.getMont(pay.getActiveId(),pay.getCreditId())
                .flatMap(responseMont -> {
                    if(responseMont.getData()!=null)
                    {
                        return CheckDebt(pay,log,paymentService,responseMont.getData());
                    }
                    else
                        return Mono.just(ResponseHandler.response("Mont Not Found", HttpStatus.BAD_REQUEST, null));
                });
    }

    public static Mono<ResponseEntity<Object>> CheckActiveType(Payment pay, Logger log, PaymentService paymentService, ActiveService activeService)
    {
        return activeService.findType(pay.getTypePayment().getValue(),pay.getActiveId())
                .flatMap(responseActive ->
                {
                    if(responseActive.getData())
                        return CheckCreditMont(pay, log, paymentService,activeService);

                    else
                        return Mono.just(ResponseHandler.response("Active Not Found", HttpStatus.BAD_REQUEST, null));
                });
    }

    public static Mono<ResponseEntity<Object>> CreatePaymentSequence(Payment pay, Logger log, PaymentService paymentService, ActiveService activeService)
    {
        return CheckActiveType(pay, log, paymentService,activeService);
    }

}
