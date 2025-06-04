package com.server.blockchainpchelincevrybakovkiryak;

import jakarta.annotation.PostConstruct; // Для метода, который будет вызван после инициализации бина
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service // Объявляем класс как компонент Spring Service
public class BlockchainService {

    private final List<Block> chain;          // Основная цепочка блоков
    private final List<Payment> pendingPayments; // Платежи, ожидающие включения в следующий блок
    private final int BLOCK_SIZE_LIMIT = 3;   // Максимальное количество платежей в одном блоке

    public BlockchainService() {
        this.chain = new ArrayList<>();
        this.pendingPayments = new ArrayList<>();
    }

    // Метод, который будет вызван после инициализации бина
    @PostConstruct
    public void init() {
        // Создаем "генезис-блок" - первый блок в цепочке
        createGenesisBlock();
    }

    private void createGenesisBlock() {
        // Генезис-блок не имеет предыдущего хэша (или имеет "0")
        // и может содержать пустой список платежей или начальный платеж
        Block genesisBlock = new Block(0, Collections.emptyList(), "0");
        chain.add(genesisBlock);
        System.out.println("Genesis Block created: " + genesisBlock.getHash());
    }

    // Метод для добавления нового платежа
    public Payment addPayment(PaymentRequest request) {
        Payment newPayment = new Payment(request.getUserId(), request.getAmount(), request.getServiceType());
        pendingPayments.add(newPayment);
        System.out.println("Payment added to pending: " + newPayment.getId() + " by " + newPayment.getUserId());

        // Если количество ожидающих платежей достигло лимита, создаем новый блок
        if (pendingPayments.size() >= BLOCK_SIZE_LIMIT) {
            minePendingPayments();
        }
        return newPayment;
    }

    // Метод для создания нового блока из ожидающих платежей
    public Block minePendingPayments() {
        if (pendingPayments.isEmpty()) {
            System.out.println("No pending payments to mine.");
            return null;
        }

        // Берем хэш последнего блока в цепочке
        String previousHash = chain.get(chain.size() - 1).getHash();
        long newBlockIndex = chain.size();

        // Создаем новый блок, используя текущие ожидающие платежи
        // Создаем копию списка и очищаем оригинальный список
        List<Payment> paymentsToInclude = new ArrayList<>(pendingPayments);
        pendingPayments.clear(); // Очищаем список ожидающих платежей

        Block newBlock = new Block(newBlockIndex, paymentsToInclude, previousHash);
        chain.add(newBlock); // Добавляем новый блок в цепочку

        System.out.println("New Block #" + newBlock.getIndex() + " mined with " + paymentsToInclude.size() + " payments. Hash: " + newBlock.getHash());
        System.out.println("Previous Block Hash: " + newBlock.getPreviousHash());
        return newBlock;
    }

    // Метод для получения всей цепочки блоков
    public List<Block> getBlockchain() {
        return Collections.unmodifiableList(chain); // Возвращаем неизменяемую копию цепочки
    }

    // Метод для получения списка ожидающих платежей
    public List<Payment> getPendingPayments() {
        return Collections.unmodifiableList(pendingPayments); // Возвращаем неизменяемую копию
    }

    // (Опционально) Метод для проверки целостности цепочки (очень упрощенно)
    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Проверяем, что хэш текущего блока действительно вычислен правильно
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current block hash is invalid for block #" + currentBlock.getIndex());
                return false;
            }

            // Проверяем, что previousHash текущего блока совпадает с хэшем предыдущего блока
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                System.out.println("Previous block hash mismatch for block #" + currentBlock.getIndex());
                return false;
            }
        }
        return true; // Цепочка валидна
    }
}