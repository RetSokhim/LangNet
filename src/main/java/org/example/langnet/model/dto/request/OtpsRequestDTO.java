package org.example.langnet.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OtpsRequestDTO {
    private Long otpsCode;
    private LocalDateTime issuedAt;
    private LocalDateTime expirationDate;
    private Boolean verify;
    private UUID user;
}
