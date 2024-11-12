package com.Todo.repository.querydsl;

import com.Todo.domain.Todo;
import com.Todo.domain.User;

import java.util.List;

public interface TodoRepositoryCustom {

    List<Todo> findTodoByUsernameOrderByDueDateAsc(String username);
    List<Todo> findTodoByUsernameAndPriority(String username, String priority);
    List<Todo> findTodoByUsernameOrderByPriorityDesc(String username);
}
