package com.pos.medicineConsults;

import com.pos.medicineConsults.exceptions.*;
import com.pos.medicineConsults.model.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    private ExceptionDTO conflictExceptionHandler(ConflictException ex) {
        return new ExceptionDTO(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UnProcessableEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    private ExceptionDTO unProcessableEntityExceptionHandler(UnProcessableEntityException ex) {
        return new ExceptionDTO(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ExceptionDTO notFoundExceptionHandler(NotFoundException ex) {
        return new ExceptionDTO(ex.getMessage());
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
}
