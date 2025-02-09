package com.moneyfli.user_service.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String phoneNo;
    private int age;

}
