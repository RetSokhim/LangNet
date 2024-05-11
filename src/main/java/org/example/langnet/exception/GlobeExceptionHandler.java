package org.example.langnet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobeExceptionHandler {
    @ExceptionHandler(AccountVerificationException.class)
    public ProblemDetail accountVerificationException(AccountVerificationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
                e.getMessage());
        problemDetail.setTitle("FORBIDDEN");
        problemDetail.setProperty("Time Stamp", LocalDateTime.now());
        return problemDetail;
    }
    @ExceptionHandler(PasswordException.class)
    public ProblemDetail passwordException (PasswordException e){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                e.getMessage());
        problemDetail.setTitle("UNAUTHORIZED");
        problemDetail.setProperty("Time Stamp",LocalDateTime.now());
        return problemDetail;
    }

}
