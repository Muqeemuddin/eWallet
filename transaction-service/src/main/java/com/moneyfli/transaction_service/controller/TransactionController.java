package com.moneyfli.transaction_service.controller;

import com.moneyfli.transaction_service.dto.CreateTransactionRequest;
import com.moneyfli.transaction_service.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/initiate")
    public ResponseEntity<String> initiateTransaction(@RequestBody @Valid CreateTransactionRequest createTransactionRequest) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((User) principal).getUsername();
        transactionService.initiateTransaction(username, createTransactionRequest);
        return ResponseEntity.ok("Transaction initiated");
    }
}
