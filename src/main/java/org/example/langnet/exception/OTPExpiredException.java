package org.example.langnet.exception;

public class OTPExpiredException extends Exception{
    public OTPExpiredException(String message) {
        super(message);
    }
}

