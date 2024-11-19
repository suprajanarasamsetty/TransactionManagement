package com.demo.controller;

import com.demo.entity.Transaction;
import com.demo.entity.TransactionStatus;
import com.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam("user_id") Long userId) {
        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long transactionId) {
        Optional<Transaction> transaction = transactionService.getTransactionById(transactionId);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Transaction> updateTransactionStatus(@PathVariable Long transactionId,
                                                                @RequestBody Map<String, String> statusUpdate) {
        String statusString = statusUpdate.get("status");
        TransactionStatus status = TransactionStatus.valueOf(statusString); // Convert string to enum
        
        Transaction updatedTransaction = transactionService.updateTransactionStatus(transactionId, status);
        return updatedTransaction != null
                ? new ResponseEntity<>(updatedTransaction, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
