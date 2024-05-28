package org.example.langnet.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.langnet.model.entity.AppUser;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDTO {
    private String baseLanguage;
    private UUID language;
    private UUID project;
    private UUID postedBy;
    private String attachmentName;
    private String data;
    private List<UUID> hints;
    private LocalDateTime postedDate;
    private LocalDateTime expireDate;
}
