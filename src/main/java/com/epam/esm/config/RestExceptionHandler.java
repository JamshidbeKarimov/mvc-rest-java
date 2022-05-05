package com.epam.esm.config;

import com.epam.esm.DTO.response.BaseExceptionDto;
import com.epam.esm.DTO.response.BaseResponseDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<?> nullPointerExceptionHandler(NullPointerException exception) {

        return ResponseEntity.ok(new BaseExceptionDto(0, "null pointer exception", 10500));
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ResponseEntity<?> emptyResultDataExceptionHandler(Exception e){
        return ResponseEntity.ok(
                new BaseResponseDto<>(-1, e.getLocalizedMessage() + e.getCause())
        );
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolationExceptionHandler(){
        return ResponseEntity.ok(
                new BaseResponseDto<>(-1, "this action violets foreign key constraint in PostgreSQL")
        );
    }

    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<?> classNotFoundExceptionHandler(Exception e){
        return ResponseEntity.ok(
                new BaseExceptionDto(500, e.getLocalizedMessage(), 10500)
        );
    }



//    @ExceptionHandler(value = HttpClientErrorException.BadRequest.class)
//    public ResponseEntity<?> badRequestErrorHandler(){
//        return new ResponseEntity<>(new BaseExceptionDto(404, "page not found", 01404), HttpStatus.BAD_REQUEST);
//    }
}
