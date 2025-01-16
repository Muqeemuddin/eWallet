package com.moneyfli.transaction_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateTransactionRequest {

    @NotBlank
    private String receiverWalletId;

    @Min(1)
    private Long amount;

    private String description;
}
