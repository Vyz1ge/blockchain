package com.server.blockchainpchelincevrybakovkiryak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String userId;          // ID пользователя
    private BigDecimal amount;      // Сумма
    private String serviceType;     // Тип услуги
}