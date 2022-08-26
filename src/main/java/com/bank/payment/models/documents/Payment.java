package com.bank.payment.models.documents;

import com.bank.payment.models.enums.TypePayment;
import com.bank.payment.models.utils.Audit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "payments")
public class Payment extends Audit
{
    @Id
    private String id;
    @NotNull(message = "activeId must not be null")
    private String activeId;
    @NotNull(message = "clientId must not be null")
    private String clientId;
    @NotNull(message = "creditId must not be null")
    private String creditId;
    @NotNull(message = "mont must not be null")
    private Float mont;
    private TypePayment typePayment;

    private String pasiveId;
    private String debitCardId;
}
