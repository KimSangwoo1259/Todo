package com.Todo.dto.response;

import com.Todo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinResponse {
    private long userId;

    public static UserJoinResponse fromEntity(User user){
        return new UserJoinResponse(user.getId());
    }
}
