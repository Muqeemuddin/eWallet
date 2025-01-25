package com.moneyfli.wallet_service.service;

import com.moneyfli.wallet_service.kafka.TransactionKafkaProducer;
import com.moneyfli.wallet_service.model.Wallet;
import com.moneyfli.wallet_service.repository.WalletDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    private WalletDao walletDao;

    @Autowired
    private TransactionKafkaProducer transactionKafkaProducer;

    private static final String SUCCESSCODE = "200";

    private static final Long InitialBalance = 1000L;

    public void createWallet(String message) {
        String mobileNo = message.split(":")[1].substring(1);
        if(mobileNo.length() != 10) {
            throw new IllegalArgumentException("Mobile number should be of 10 digits");
        }

        walletDao.save(Wallet.builder().walletId(mobileNo).balance(InitialBalance).currency("USD").build());
    }

    public void updateWallet(String message) {
        String[] messageParts = message.split(",");
        String senderWalletId = messageParts[1].split(":")[1].trim();
        String receiverWalletId = messageParts[2].split(":")[1].trim();
        Long amount = Long.parseLong(messageParts[3].split(":")[1].trim());

        Wallet senderWallet = walletDao.findByWalletId(senderWalletId);
        Wallet receiverWallet = walletDao.findByWalletId(receiverWalletId);

        if(senderWallet.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        senderWallet.setBalance(senderWallet.getBalance() - amount);
        receiverWallet.setBalance(receiverWallet.getBalance() + amount);

        try{
            walletDao.save(senderWallet);
            walletDao.save(receiverWallet);

            // send message to kafka

            transactionKafkaProducer.sendTransactionCompleteMessage("For "+messageParts[0]+", Wallets Successfully Updated" + SUCCESSCODE);

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error while updating wallet");
        }

    }
}
