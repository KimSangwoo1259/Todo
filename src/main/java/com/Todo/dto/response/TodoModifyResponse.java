package com.Todo.dto.response;

import com.Todo.domain.Priority;
import com.Todo.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class TodoModifyResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Priority priority;
    private boolean completed;

    public static TodoModifyResponse fromEntity(Todo todo) {
        return new TodoModifyResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getDueDate(),
                todo.getPriority(),
                todo.getCompleted());
    }
}
