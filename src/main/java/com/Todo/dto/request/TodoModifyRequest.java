package com.Todo.dto.request;

import com.Todo.domain.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class TodoModifyRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Priority priority;
    private Boolean completed;

}
