package com.server.blockchainpchelincevrybakovkiryak;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Объявляем класс как REST контроллер
@RequestMapping("/api/blockchain") // Базовый путь для всех эндпоинтов в этом контроллере
public class BlockchainController {

    private final BlockchainService blockchainService; // Внедряем сервис блокчейна

    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    // POST /api/blockchain/payment
    // Эндпоинт для отправки нового платежа
    @PostMapping("/payment")
    public ResponseEntity<String> addPayment(@RequestBody PaymentRequest request) {
        if (request.getUserId() == null || request.getUserId().isEmpty() ||
                request.getAmount() == null || request.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0 ||
                request.getServiceType() == null || request.getServiceType().isEmpty()) {
            return new ResponseEntity<>("Invalid payment data.", HttpStatus.BAD_REQUEST);
        }
        blockchainService.addPayment(request);
        return new ResponseEntity<>("Payment added. A new block will be mined when " +
                "pending payments reach the limit.", HttpStatus.ACCEPTED);
    }

    // GET /api/blockchain/chain
    // Эндпоинт для просмотра всей цепочки блоков
    @GetMapping("/chain")
    public ResponseEntity<List<Block>> getBlockchain() {
        return new ResponseEntity<>(blockchainService.getBlockchain(), HttpStatus.OK);
    }

    // GET /api/blockchain/pending-payments
    // Эндпоинт для просмотра ожидающих платежей (не вошедших в блок)
    @GetMapping("/pending-payments")
    public ResponseEntity<List<Payment>> getPendingPayments() {
        return new ResponseEntity<>(blockchainService.getPendingPayments(), HttpStatus.OK);
    }

    // GET /api/blockchain/is-valid
    // Эндпоинт для проверки целостности цепочки
    @GetMapping("/is-valid")
    public ResponseEntity<String> isChainValid() {
        if (blockchainService.isChainValid()) {
            return new ResponseEntity<>("Blockchain is valid!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Blockchain is NOT valid! Tampering detected.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}