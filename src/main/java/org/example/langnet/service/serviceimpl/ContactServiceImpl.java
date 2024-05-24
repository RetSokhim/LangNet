package org.example.langnet.service.serviceimpl;

import org.example.langnet.model.dto.request.ContactRequest;
import org.example.langnet.repository.ContactRepository;
import org.example.langnet.service.ContactService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void addContactToUser(ContactRequest contactRequest, UUID userId) {
        contactRepository.addContactToUser(contactRequest,userId);
    }
}
