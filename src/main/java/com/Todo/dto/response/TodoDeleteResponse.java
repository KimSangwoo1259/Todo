package com.Todo.dto.response;

import com.Todo.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TodoDeleteResponse {

    private Long id;

    public static TodoDeleteResponse fromEntity(Todo todo){
        return new TodoDeleteResponse(todo.getId());
    }

}
