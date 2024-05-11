package org.example.langnet.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    private UUID userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime birthDate;
    private String password;
    private String image;
    private String gender;
    private String phoneNumber;
    private String telegram;
    private String facebook;
}
