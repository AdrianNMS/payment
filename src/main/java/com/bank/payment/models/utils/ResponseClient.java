package com.bank.payment.models.utils;

import lombok.Data;

@Data
public class ResponseClient
{
    private Integer data;

    private String message;

    private String status;
}
