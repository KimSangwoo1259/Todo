package com.Todo.repository.querydsl;

import com.Todo.domain.Priority;
import com.Todo.domain.QTodo;
import com.Todo.domain.Todo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory factory;
    QTodo todo = QTodo.todo;

    @Override
    public List<Todo> findTodoByUsernameOrderByDueDateAsc(String username) { // 사용자에 맞는 todolist를 마감기한이 임박한 순으로 부터
        return factory.selectFrom(todo)
                .where(todo.user.username.eq(username))
                .orderBy(todo.dueDate.asc())
                .fetch();
    }

    @Override
    public List<Todo> findTodoByUsernameAndPriority(String username, String priority) {
        Priority p = Priority.valueOf(priority);

        return factory.selectFrom(todo)
                .where(todo.user.username.eq(username).and(todo.priority.eq(p)))
                .fetch();
    }

    @Override
    public List<Todo> findTodoByUsernameOrderByPriorityDesc(String username) {
        return factory.selectFrom(todo)
                .where(todo.user.username.eq(username))
                .orderBy(todo.priority.desc())
                .fetch();
    }
}
