package com.moneyfli.user_service.dto;

import lombok.Builder;

@Builder
public class CustomerResponse {
    
    private String name;
    private String email;
    private String phoneNo;
    private int age;
    
    
}
