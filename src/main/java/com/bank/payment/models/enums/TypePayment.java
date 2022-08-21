package com.bank.payment.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypePayment
{
    PERSONAL(2000),
    COMPANY(2001);

    private final int value;

    public static TypePayment fromInteger(int val) {
        switch(val) {
            case 0:
                return PERSONAL;
            case 1:
                return COMPANY;

        }
        return null;
    }
}