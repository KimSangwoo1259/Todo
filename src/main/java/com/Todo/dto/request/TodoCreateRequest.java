package com.Todo.dto.request;

import com.Todo.domain.Priority;
import com.Todo.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class TodoCreateRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Priority priority;
    private List<String> tags;

}
