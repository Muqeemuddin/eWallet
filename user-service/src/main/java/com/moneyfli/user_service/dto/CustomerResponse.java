package com.moneyfli.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    private String name;
    private String email;
    private String phoneNo;
    private int age;

    
}
