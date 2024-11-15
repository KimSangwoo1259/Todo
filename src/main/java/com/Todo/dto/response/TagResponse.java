package com.Todo.dto.response;

import com.Todo.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TagResponse {
    String name;

    public static TagResponse fromEntity(Tag tag) {
        return new TagResponse(tag.getName());
    }
}
