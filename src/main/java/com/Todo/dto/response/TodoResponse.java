package com.Todo.dto.response;

import com.Todo.domain.Priority;
import com.Todo.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
public class TodoResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Priority priority;
    private Boolean completed;

    public static TodoResponse fromEntity(Todo todo) {
        return new TodoResponse(todo.getId(), todo.getDescription(), todo.getDescription(), todo.getDueDate(), todo.getPriority(), todo.getCompleted());
    }
}
