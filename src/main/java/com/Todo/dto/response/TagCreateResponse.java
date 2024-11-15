package com.Todo.dto.response;

import com.Todo.domain.Tag;
import com.Todo.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TagCreateResponse {
    private Long id;
    private Long todoId;

    public static TagCreateResponse fromEntity(Tag tag, Todo todo) {
        return new TagCreateResponse(tag.getId(), todo.getId());
    }
}
