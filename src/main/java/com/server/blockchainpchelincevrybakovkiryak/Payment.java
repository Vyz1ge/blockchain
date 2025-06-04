package com.server.blockchainpchelincevrybakovkiryak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data // Генерирует геттеры, сеттеры, equals, hashCode, toString
@NoArgsConstructor // Генерирует конструктор без аргументов
@AllArgsConstructor // Генерирует конструктор со всеми аргументами
public class Payment {
    private String id;              // Уникальный идентификатор платежа
    private String userId;          // Идентификатор пользователя, совершившего платеж
    private BigDecimal amount;      // Сумма платежа
    private String serviceType;     // Вид услуги (вода, электричество, газ и т.д.)
    private Instant timestamp;      // Время совершения платежа

    // Конструктор для создания нового платежа
    public Payment(String userId, BigDecimal amount, String serviceType) {
        this.id = UUID.randomUUID().toString(); // Генерируем уникальный ID
        this.userId = userId;
        this.amount = amount;
        this.serviceType = serviceType;
        this.timestamp = Instant.now(); // Устанавливаем текущее время
    }
}