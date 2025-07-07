package com.github.nelsonssoares.AuthGateway.exceptions;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.TransactionalException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ExceptionResponse> handleConstraintVaiolationException(ConstraintViolationException e, HttpServletRequest http){
        ExceptionResponse error = new ExceptionResponse();

        error.setError("Falha na validação dos campos: ");
        error.setPath(http.getRequestURI());
        error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        error.setTimestamp(Instant.now());

        List<FieldError> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            FieldError err = new FieldError(null,null);
            err.setField(violation.getPropertyPath().toString());
            err.setMessage(violation.getMessage());
            errors.add(err);

        }
        error.setFields(errors);

        return ResponseEntity.unprocessableEntity().body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest http) {
        ExceptionResponse error = new ExceptionResponse();
        error.setError("Falha na validação dos campos: ");
        error.setPath(http.getRequestURI());
        error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        error.setTimestamp(Instant.now());
        List<FieldError> fieldErrors = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            fieldError.setField(ex.getBindingResult().getFieldError().getField());
            fieldError.setMessage(ex.getBindingResult().getFieldError().getDefaultMessage());
            fieldErrors.add(fieldError);
        }
        error.setFields(fieldErrors);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleNullPointerException(NullPointerException e, HttpServletRequest http) {

        ExceptionResponse error = new ExceptionResponse();
        error.setTimestamp(Instant.now());
        error.setError("Argumentos ou parâmetros invalidos!");
        error.setPath(http.getRequestURI());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        List<FieldError> fieldErrors = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            fieldError.setField("Unknown");
            fieldError.setMessage(e.getMessage());
            fieldErrors.add(fieldError);
        }
        error.setFields(fieldErrors);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(TransactionalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleITransactionException(TransactionalException e,
                                                                         HttpServletRequest http) {
        ExceptionResponse error = new ExceptionResponse();
        error.setTimestamp(Instant.now());
        error.setError("Erro de validação, verifique se as informações e os campos estão corretos");
        error.setPath(http.getRequestURI());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        List<FieldError> fieldErrors = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            fieldError.setField("Unknown");
            fieldError.setMessage(e.getMessage());
            fieldErrors.add(fieldError);
        }
        error.setFields(fieldErrors);

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException dataIntegrityViolationException, HttpServletRequest http) {
        ExceptionResponse error = new ExceptionResponse();
        error.setError("Operação ja foi executada, ou existente!");
        error.setPath(http.getRequestURI());
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setTimestamp(Instant.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }



    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleObjectNotFoundException(ObjectNotFoundException e,
                                                                           HttpServletRequest http) {

        ExceptionResponse error = new ExceptionResponse();
        error.setError("Falha ao tentar encontrar o elemento requisitado");
        error.setPath(http.getRequestURI());
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setTimestamp(Instant.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(FileNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleFileNotFoundException(Exception ex, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setTimestamp(Instant.now());
        exceptionResponse.setError("Falha ao tentar encontrar o arquivo requisitado");
        exceptionResponse.setPath(request.getDescription(false));
        exceptionResponse.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileStorageException.class)
    public final ResponseEntity<ExceptionResponse> handleFileStorageException(Exception ex, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setTimestamp(Instant.now());
        exceptionResponse.setError("Falha ao tentar armazenar o arquivo requisitado");
        exceptionResponse.setPath(request.getDescription(false));
        exceptionResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleFeignException(FeignException e, HttpServletRequest http) {
        ExceptionResponse error = new ExceptionResponse();
        error.setError("Erro ao comunicar com o serviço externo: " + e.getMessage());
        error.setPath(http.getRequestURI());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setTimestamp(Instant.now());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationException(InvalidJwtAuthenticationException e, HttpServletRequest http) {
        ExceptionResponse error = new ExceptionResponse();
        error.setError("Token JWT inválido ou expirado: " + e.getMessage());
        error.setPath(http.getRequestURI());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setTimestamp(Instant.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }


}
