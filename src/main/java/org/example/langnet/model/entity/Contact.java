package org.example.langnet.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private UUID contactId;
    private String facebook;
    private String phoneNumber;
    private String telegram;

}
