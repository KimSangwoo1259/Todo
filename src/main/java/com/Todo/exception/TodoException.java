package com.Todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;
}
