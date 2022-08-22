package com.bank.payment.models.utils;

import lombok.Data;

@Data
public class ResponseDebitCard
{
    private Boolean data;
    private String message;

    private String status;
}
