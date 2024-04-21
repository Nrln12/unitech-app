package com.unitech.app.unitechapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String pin;
    private String firstname;
    private String lastname;
    private String password;
}
