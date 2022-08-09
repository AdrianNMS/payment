package com.bank.payment.models.documents;

import com.bank.payment.models.utils.Audit;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "payments")
public class Payment extends Audit
{
    @Id
    private String id;
    private String activeId;
    private String clientId;
    private String creditId;
    private Float mont;
}
