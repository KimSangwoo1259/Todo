package com.Todo.dto.response;

import com.Todo.domain.Tag;
import com.Todo.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TagModifyResponse {

    private Long id;
    private Long todoId;
    private String name;

    public static TagModifyResponse fromEntity(Tag tag, Todo todo) {
        return new TagModifyResponse(tag.getId(), todo.getId(), tag.getName());
    }

}
