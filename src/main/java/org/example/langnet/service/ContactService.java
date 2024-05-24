package org.example.langnet.service;

import org.example.langnet.model.dto.request.ContactRequest;

import java.util.UUID;

public interface ContactService {
    void addContactToUser(ContactRequest contactRequest, UUID userId);
}
