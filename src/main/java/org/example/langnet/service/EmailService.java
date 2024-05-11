package org.example.langnet.service;

import org.thymeleaf.context.Context;

public interface EmailService{
    void sendEmailWithHtmlTemplate (String toEmail, String subject, String templateName, Context context) throws Exception;
}
