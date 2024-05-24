package org.example.langnet.model.dto.respond;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {
    private UUID feedbackId;
    private UserProfileDetailResponse commentBy;
    private String comment;
    private Boolean active;
    private LocalDateTime createdDate;
    private LocalDateTime removedDate;
    private LocalDateTime editDate;
}
