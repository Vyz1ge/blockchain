package com.server.blockchainpchelincevrybakovkiryak;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Block {
    private String id;              // Уникальный идентификатор блока
    private long index;             // Порядковый номер блока в цепочке
    private Instant timestamp;      // Время создания блока
    private List<Payment> payments; // Список платежей, включенных в этот блок
    private String previousHash;    // Хэш предыдущего блока в цепочке
    private String hash;            // Хэш текущего блока

    // Конструктор для создания нового блока
    public Block(long index, List<Payment> payments, String previousHash) {
        this.id = UUID.randomUUID().toString(); // Генерируем уникальный ID блока
        this.index = index;
        this.timestamp = Instant.now(); // Устанавливаем текущее время создания
        this.payments = payments;
        this.previousHash = previousHash;
        this.hash = calculateHash(); // Вычисляем хэш блока при его создании
    }

    // Метод для вычисления хэша текущего блока
    public String calculateHash() {
        // Объединяем данные блока в одну строку для хеширования:
        // индекс + временная метка + хэш предыдущего блока + данные всех платежей
        String data = index +
                timestamp.toString() +
                previousHash +
                payments.stream()
                        .map(p -> p.getId() + p.getUserId() + p.getAmount().toPlainString() + p.getServiceType() + p.getTimestamp().toString())
                        .reduce("", String::concat); // Объединяем все данные платежей в одну строку
        return HashingUtil.applySha256(data); // Применяем SHA-256 хеширование
    }
}