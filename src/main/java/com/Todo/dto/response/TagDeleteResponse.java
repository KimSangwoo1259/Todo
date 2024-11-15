package com.Todo.dto.response;

import com.Todo.domain.Tag;
import com.Todo.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TagDeleteResponse {

    private Long id;

    public static TagDeleteResponse fromEntity(Tag tag){
        return new TagDeleteResponse(tag.getId());
    }
}
