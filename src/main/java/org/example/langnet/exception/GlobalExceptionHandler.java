package org.example.langnet.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //For handle with argument exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail methodArgumentNotValidException(MethodArgumentNotValidException e) {
        HashMap<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Invalid Input Please Try Again !");
        problemDetail.setTitle("BAD_REQUEST");
        problemDetail.setProperty("Time Stamp", LocalDateTime.now());
        problemDetail.setProperty("Errors", errors);
        return problemDetail;
    }

    //For catch search not found exception
    @ExceptionHandler(SearchNotFoundException.class)
    public ProblemDetail searchNotFoundException(SearchNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                e.getMessage());
        problemDetail.setTitle("NOT_FOUND");
        problemDetail.setProperty("Time Stamp", LocalDateTime.now());
        return problemDetail;
    }

    //For catch limit page exception
    @ExceptionHandler(PageLimitException.class)
    public ProblemDetail pageLimitException(PageLimitException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                e.getMessage());
        problemDetail.setTitle("BAD_REQUEST");
        problemDetail.setProperty("Time Stamp", LocalDateTime.now());
        return problemDetail;
    }

    //For catch @Positive Message Exception
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail constraintViolationException(ConstraintViolationException e) {
        HashMap<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> error : e.getConstraintViolations()) {
            errors.put(error.getPropertyPath().toString().substring(error.
                                    getPropertyPath().
                                    toString().
                                    indexOf(".") + 1).
                            replaceFirst("\\.", ""),
                    error.getMessage());
        }
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Invalid Input");
        problemDetail.setTitle("BAD_REQUEST");
        problemDetail.setProperty("Time Stamp", LocalDateTime.now());
        problemDetail.setProperty("Errors", errors);
        return problemDetail;
    }

    //For OTP expired
    @ExceptionHandler(OTPExpiredException.class)
    public ProblemDetail otpExpiredException(OTPExpiredException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
                e.getMessage());
        problemDetail.setTitle("FORBIDDEN");
        problemDetail.setProperty("Time Stamp", LocalDateTime.now());
        return problemDetail;
    }

    //For Email Already Exist
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ProblemDetail emailAlreadyExistException(EmailAlreadyExistException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                e.getMessage());
        problemDetail.setTitle("CONFLICT");
        problemDetail.setProperty("Time Stamp", LocalDateTime.now());
        return problemDetail;
    }

    //For wrong password and wrong confirm password
    @ExceptionHandler(PasswordException.class)
    public ProblemDetail passwordException(PasswordException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                e.getMessage());
        problemDetail.setTitle("UNAUTHORIZED");
        problemDetail.setProperty("Time Stamp", LocalDateTime.now());
        return problemDetail;
    }

    //For email verification
    @ExceptionHandler(AccountVerificationException.class)
    public ProblemDetail accountVerificationException(AccountVerificationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
                e.getMessage());
        problemDetail.setTitle("FORBIDDEN");
        problemDetail.setProperty("Time Stamp", LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail methodValidationExceptionHandler(HandlerMethodValidationException exception) {
        HashMap<String, String> errors = new HashMap<>();
        for (var parameterError : exception.getAllValidationResults()) {
            final String parameterName = parameterError.getMethodParameter().getParameterName();
            for (var error : parameterError.getResolvableErrors()) {
                errors.put(parameterName, error.getDefaultMessage());
            }
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Validation field");
        problemDetail.setTitle("BAD_REQUEST");
        problemDetail.setStatus(403);
        problemDetail.setProperty("errors", LocalDateTime.now());
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>("Invalid request body", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ProblemDetail noSuchFileException(NoSuchFileException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Invalid image's name");
        problemDetail.setTitle("NOT_FOUND");
        problemDetail.setProperty("Time Stamp", LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail badRequestException(BadRequestException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                e.getMessage());
        problemDetail.setTitle("UNAUTHORIZED");
        problemDetail.setProperty("Time Stamp", LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Please check your input and try again");
        problemDetail.setTitle("BAD_REQUEST");
        problemDetail.setProperty("Time Stamp", LocalDateTime.now());
        return problemDetail;
    }
}