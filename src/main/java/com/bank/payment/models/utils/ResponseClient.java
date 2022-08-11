package com.bank.payment.models.utils;

import com.bank.payment.models.documents.Client;
import lombok.Data;

@Data
public class ResponseClient
{
    private Client data;

    private String message;

    private String status;

}
