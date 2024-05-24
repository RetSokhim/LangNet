package org.example.langnet.model.dto.respond;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.langnet.model.entity.Contact;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDetailResponse {
    private UUID userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String image;
    private String gender;
    private Contact contact;
}
