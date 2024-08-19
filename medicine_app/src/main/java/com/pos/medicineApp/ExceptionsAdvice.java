package com.pos.medicineApp;

import com.pos.medicineApp.exceptions.*;
import com.pos.medicineApp.view.dto.ExceptionDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.ConnectException;
import java.util.Objects;

@ControllerAdvice
public class ExceptionsAdvice {

    @ResponseBody
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ExceptionDTO conflictExceptionHandler(ConflictException ex) {
        return new ExceptionDTO(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ExceptionDTO notFoundExceptionHandler(NotFoundException ex) {
        return new ExceptionDTO(ex.getMessage());
    }

    // ConstraintViolationException because it does not conform to mandatory caractheristics
    @ResponseBody
    @ExceptionHandler({UnprocessableContentException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ExceptionDTO unprocessableContentExceptionHandler(RuntimeException ex) {
        return new ExceptionDTO(ex.getMessage());
    }

    // DataIntegrityViolationException because it is thrown on unquie key, duplicate
    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ExceptionDTO dataIntegrityViolationExceptionHandler(DataIntegrityViolationException ex) {
        Throwable rootCause = ex.getRootCause();
        return new ExceptionDTO(Objects.requireNonNullElse(rootCause, ex).getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ExceptionDTO unauthorizedExceptionHandler(UnauthorizedException ex) {
        return new ExceptionDTO(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ExceptionDTO forbiddenExceptionHandler(ForbiddenException ex) {
        return new ExceptionDTO(ex.getMessage());
    }

    // when receiving a bad response from grpc server
    @ResponseBody
    @ExceptionHandler(BadGatewayException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    ExceptionDTO badGatewayExceptionHandler(BadGatewayException ex) {
        return new ExceptionDTO(ex.getMessage());
    }

    // when connection to database fails
    @ResponseBody
    @ExceptionHandler(ConnectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ExceptionDTO connectExceptionHandler(ConnectException ex) {
        return new ExceptionDTO(ex.getMessage());
    }
}
