package com.Todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USERNAME_DUPLICATED(HttpStatus.CONFLICT, "Duplicated Username"),
    USER_NOTFOUND(HttpStatus.NOT_FOUND, "User not found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid Password"),
    TODO_NOTFOUND(HttpStatus.NOT_FOUND, "Todo not found"),
    INVALID_USER(HttpStatus.UNAUTHORIZED, "Invalid User"),
    PRIORITY_NOT_FOUND(HttpStatus.NOT_FOUND, "Priority not found"),
    ;

    private HttpStatus httpStatus;
    private String message;
}
