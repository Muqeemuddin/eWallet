package com.moneyfli.user_service.controller;

import com.moneyfli.user_service.dto.CreateCustomerRequest;
import com.moneyfli.user_service.dto.CustomerResponse;
import com.moneyfli.user_service.dto.LoginRequest;
import com.moneyfli.user_service.service.CustomerService;
import com.moneyfli.user_service.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest){
        final String result = customerService.verify(loginRequest);
        System.out.println(result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/profile-info")
    public CustomerResponse getCustomer() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((User) principal).getUsername();
        return Utils.fromCustomer(customerService.getCustomerByPhoneNo(username));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<UserDetails> validateToken(@RequestHeader("Authorization") String authorization) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((User) principal).getUsername();
        return ResponseEntity.ok(customerService.loadUserByUsername(username));
    }

    @GetMapping("/getEmail/{username}")
    public ResponseEntity<String> getEmail(@PathVariable String username) {
        return ResponseEntity.ok(customerService.getCustomerByPhoneNo(username).getEmail());
    }
// localhost:8080/moneyfli/v1/customer/validate-token
}
