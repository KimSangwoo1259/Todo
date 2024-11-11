package com.Todo.dto.response;

import com.Todo.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TodoModifyResponse {
    private Long id;

    public static TodoModifyResponse fromEntity(Todo todo) {
        return new TodoModifyResponse(todo.getId());
    }
}
