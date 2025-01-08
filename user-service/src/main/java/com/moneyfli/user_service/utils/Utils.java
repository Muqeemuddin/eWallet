package com.moneyfli.user_service.utils;

import com.moneyfli.user_service.dto.CreateCustomerRequest;
import com.moneyfli.user_service.model.Customer;

public class Utils {

    public static Customer toCustomer(CreateCustomerRequest createCustomerRequest) {
        return Customer.builder()
            .name(createCustomerRequest.getName())
            .email(createCustomerRequest.getEmail())
            .password(createCustomerRequest.getPassword())
            .phoneNo(createCustomerRequest.getPhoneNo())
            .age(createCustomerRequest.getAge())
            .build();
    }

    public static Customer fromCustomer(Customer customer) {
        return Customer.builder()
            .name(customer.getName())
            .email(customer.getEmail())
            .phoneNo(customer.getPhoneNo())
            .age(customer.getAge())
            .build();
    }
}
