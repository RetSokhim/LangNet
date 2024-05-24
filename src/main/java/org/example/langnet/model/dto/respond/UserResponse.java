package org.example.langnet.model.dto.respond;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.langnet.model.entity.Contact;
import org.example.langnet.model.entity.Project;
import org.example.langnet.model.entity.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String image;
    private String gender;
    private Contact contact;
    private List<ProjectResponse> projects;
}
