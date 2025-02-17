package org.example.langnet.model.dto.respond;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.langnet.model.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentResponse {
    private UUID attachmentId;
    private String baseLanguage;
    private Language language;
    private UserProfileDetailResponse postBy;
    private List<Feedback> feedback;
    private String attachmentName;
    private String data;
    private List<Hint> hints;
    private Status status;
    private LocalDateTime postedDate;
    private LocalDateTime expireDate;
}
