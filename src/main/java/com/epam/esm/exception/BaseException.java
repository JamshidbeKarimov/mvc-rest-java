package com.epam.esm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends RuntimeException{
    private int status;
    private String message;

    public BaseException(String message, int status) {
        this.status = status;
        this.message = message;
    }
}
