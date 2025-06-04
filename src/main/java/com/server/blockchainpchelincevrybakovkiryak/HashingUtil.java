package com.server.blockchainpchelincevrybakovkiryak;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class HashingUtil {

    // Применяет SHA-256 хеширование к входной строке
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Получаем экземпляр SHA-256
            // Применяем хеш к входной строке
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(); // Использование StringBuilder для эффективной конкатенации
            for (byte b : hash) {
                // Преобразуем каждый байт в шестнадцатеричное представление
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0'); // Добавляем ведущий ноль, если нужно
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Обрабатываем исключение, если алгоритм SHA-256 не найден (маловероятно)
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}