package org.example.langnet.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentRequest {
    private UUID language;
    private UUID languageTranslateTo;
    private UUID project;
    private String attachmentName;
    private String data;
    private List<UUID> hints;
    private LocalDateTime postedDate;
    private LocalDateTime expireDate;
}
