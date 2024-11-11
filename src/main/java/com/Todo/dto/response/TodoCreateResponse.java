package com.Todo.dto.response;

import com.Todo.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TodoCreateResponse {
    Long todoId;

    public static TodoCreateResponse fromEntity(Todo todo) {
        return new TodoCreateResponse(todo.getId());
    }
}
