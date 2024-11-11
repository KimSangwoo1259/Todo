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
    INVALUD_USER(HttpStatus.UNAUTHORIZED, "Invalid User"),
    ;

    private HttpStatus httpStatus;
    private String message;
}
