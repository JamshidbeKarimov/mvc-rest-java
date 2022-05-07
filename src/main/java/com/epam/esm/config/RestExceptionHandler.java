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
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    //use logging for seeing exception || use custom exception
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<?> nullPointerExceptionHandler(Exception e) {
        return ResponseEntity.ok(
                new BaseExceptionDto(0, e.getLocalizedMessage(), 10500)
        );
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ResponseEntity<?> emptyResultDataExceptionHandler(Exception e){
        return ResponseEntity.ok(
                new BaseResponseDto<>(-1, e.getLocalizedMessage())
        );
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolationExceptionHandler(Exception e){
        return ResponseEntity.ok(
                new BaseResponseDto<>(-1, e.getLocalizedMessage())
        );
    }

    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<?> classNotFoundExceptionHandler(Exception e){
        return ResponseEntity.ok(
                new BaseExceptionDto(500, e.getLocalizedMessage(), 10500)
        );
    }
}
