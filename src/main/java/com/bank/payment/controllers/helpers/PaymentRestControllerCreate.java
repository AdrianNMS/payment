package com.bank.payment.controllers.helpers;

import com.bank.payment.handler.ResponseHandler;
import com.bank.payment.models.documents.Payment;
import com.bank.payment.models.enums.TypePayment;
import com.bank.payment.models.utils.Mont;
import com.bank.payment.services.ActiveService;
import com.bank.payment.services.PasiveService;
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

    public static Mono<ResponseEntity<Object>> setMontPasive(Payment pay,Logger log, PasiveService pasive, PaymentService paymentService, Mont mont) {
        return pasive.setMont(pay.getPasiveId(), mont)
                .flatMap(responseMont -> {
                    if (responseMont.getStatus().equals("OK"))
                        return createPayment(pay, log, paymentService);
                    else
                        return Mono.just(ResponseHandler.response("Error", HttpStatus.BAD_REQUEST, null));

                });
    }



    public static Mono<ResponseEntity<Object>> getMontPasive(Payment pay,Logger log, PasiveService pasiveService, PaymentService paymentService)
    {
        return pasiveService.getMont(pay.getPasiveId()).flatMap(responseMont -> {
            if (responseMont.getData() != null)
            {
                float dif =  responseMont.getData().getMont() - pay.getMont();

                if (dif >= 0)
                {
                    Mont mont = new Mont();
                    mont.setMont(-pay.getMont());
                    log.info(mont.toString());
                    return setMontPasive(pay,log,pasiveService,paymentService,mont);
                }
                else
                    return Mono.just(ResponseHandler.response("You don't have enough credit", HttpStatus.BAD_REQUEST, null));

            }
            else
                return Mono.just(ResponseHandler.response("Pasive Not Found", HttpStatus.BAD_REQUEST, null));
        });
    }

    public static Mono<ResponseEntity<Object>> payWithDebitCard(Payment pay,Logger log, PasiveService pasiveService, PaymentService paymentService)
    {
        Mont mont = new Mont();
        mont.setMont(pay.getMont());

        return pasiveService.payWithDebitCard(pay.getDebitCardId(),mont)
                .flatMap(responseDebitCard -> {
                    if(responseDebitCard.getData())
                    {
                        return createPayment(pay, log, paymentService);
                    }
                    else
                    {
                        return Mono.just(ResponseHandler.response("You don't have enough credits", HttpStatus.BAD_REQUEST, null));
                    }
                });
    }


    public static Mono<ResponseEntity<Object>> CheckActiveType(Payment pay, Logger log, PaymentService paymentService, ActiveService activeService, PasiveService pasive)
    {
        log.info(pay.toString());
        return activeService.findType(pay.getActiveId())
                .flatMap(responseActive ->
                {
                    log.info(responseActive.toString());
                    if(responseActive.getData()!=null)
                    {
                        pay.setTypePayment(TypePayment.fromInteger(responseActive.getData()));

                        if(pay.getDebitCardId()==null || pay.getDebitCardId().isEmpty())
                            return getMontPasive(pay, log, pasive,paymentService);
                        else
                            return payWithDebitCard(pay, log, pasive,paymentService);
                    }
                    else
                        return Mono.just(ResponseHandler.response("Active Not Found", HttpStatus.BAD_REQUEST, null));
                });
    }

    public static Mono<ResponseEntity<Object>> CreatePaymentSequence(Payment pay, Logger log, PaymentService paymentService, ActiveService activeService, PasiveService pasive)
    {
        return CheckActiveType(pay, log, paymentService,activeService, pasive);
    }

}
