package com.moneyfli.transaction_service.dao;

import com.moneyfli.transaction_service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDao extends JpaRepository<Transaction,Long> {
}
