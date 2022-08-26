package com.bank.payment.models.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseDebitCard
{
    private Boolean data;
    private String message;

    private String status;
}
