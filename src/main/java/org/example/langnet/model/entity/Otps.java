package org.example.langnet.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Otps {
    private UUID otpsId;
    private Long otpsCode;
    private Boolean verify;
    private Boolean active;
    private LocalDateTime issuedAt;
    private LocalDateTime expirationDate;
    private AppUser user;
}
