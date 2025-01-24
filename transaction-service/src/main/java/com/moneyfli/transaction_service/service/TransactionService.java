package com.moneyfli.transaction_service.service;

import com.moneyfli.transaction_service.dao.TransactionDao;
import com.moneyfli.transaction_service.dto.CreateTransactionRequest;
import com.moneyfli.transaction_service.model.Transaction;
import com.moneyfli.transaction_service.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionDao transactionDao;

    public void initiateTransaction(String username,  CreateTransactionRequest createTransactionRequest) {

        String transactionId = "TRX" + UUID.randomUUID().toString().substring(0, 6);
        // initiate transaction
        Transaction transaction = Transaction.builder()
                .senderWalletId(username)
                .receiverWalletId(createTransactionRequest.getReceiverWalletId())
                .amount(createTransactionRequest.getAmount())
                .description(createTransactionRequest.getDescription())
                .transactionStatus(TransactionStatus.valueOf("PENDING"))
                .transactionId(transactionId)
                .build();

        transactionDao.save(transaction);



    }
}
