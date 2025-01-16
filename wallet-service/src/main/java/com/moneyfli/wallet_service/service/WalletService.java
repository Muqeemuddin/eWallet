package com.moneyfli.wallet_service.service;

import com.moneyfli.wallet_service.model.Wallet;
import com.moneyfli.wallet_service.repository.WalletDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    private WalletDao walletDao;

    private static final Long InitialBalance = 1000L;

    public void createWallet(String message) {
        String mobileNo = message.split(":")[1].substring(1);
        if(mobileNo.length() != 10) {
            throw new IllegalArgumentException("Mobile number should be of 10 digits");
        }

        walletDao.save(Wallet.builder().walletId(mobileNo).balance(InitialBalance).currency("USD").build());
    }
}
