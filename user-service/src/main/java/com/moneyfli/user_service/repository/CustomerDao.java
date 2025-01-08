package com.moneyfli.user_service.repository;

import com.moneyfli.user_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDao extends JpaRepository<Customer, Long> {
    Customer findByPhoneNo(String phoneNo);
}
