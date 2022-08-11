package com.bank.payment.models.utils;

import com.bank.payment.models.documents.Active;
import lombok.Data;

@Data
public class ResponseActive
{
    private Active data;

    private String message;

    private String status;

}
