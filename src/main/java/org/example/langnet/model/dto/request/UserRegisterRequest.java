package org.example.langnet.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private LocalDateTime birthDate;
    private String password;
}