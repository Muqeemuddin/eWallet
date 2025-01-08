package com.moneyfli.user_service.controller;

import com.moneyfli.user_service.dto.CreateCustomerRequest;
import com.moneyfli.user_service.model.Customer;
import com.moneyfli.user_service.service.CustomerService;
import com.moneyfli.user_service.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/moneyfli/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @PostMapping("/create-customer")
    public ResponseEntity<String> createCustomer(@RequestBody @Valid CreateCustomerRequest createCustomerRequest) {
        String result = customerService.createOrUpdateCustomer(Utils.toCustomer(createCustomerRequest));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-customer")
    public Customer getCustomer() {
         Customer customer = (Customer)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         return Utils.fromCustomer(customer);
    }
}
