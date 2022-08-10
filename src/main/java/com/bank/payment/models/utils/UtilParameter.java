package com.bank.payment.models.utils;

import com.bank.payment.models.documents.Parameter;

import java.util.List;
import java.util.stream.Collectors;

public class UtilParameter {

    public List<Parameter> getParameter(List<Parameter> listParameter, Integer code) {

        return listParameter.stream().filter(x -> x.getCode().toString().equals(code.toString()) )
                .collect(Collectors.toList());
    }
}
