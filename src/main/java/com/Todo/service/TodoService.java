package com.Todo.service;

import com.Todo.domain.Todo;
import com.Todo.domain.User;
import com.Todo.dto.request.TodoCreateRequest;
import com.Todo.dto.request.TodoDeleteRequest;
import com.Todo.dto.request.TodoModifyRequest;
import com.Todo.dto.response.TodoCreateResponse;
import com.Todo.dto.response.TodoDeleteResponse;
import com.Todo.dto.response.TodoModifyResponse;
import com.Todo.exception.ErrorCode;
import com.Todo.exception.TodoException;
import com.Todo.repository.TodoRepository;
import com.Todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Service
public class TodoService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public TodoCreateResponse createTodo(TodoCreateRequest request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TodoException(ErrorCode.USER_NOTFOUND, String.format("%s is not found", username)));
        Todo todo = todoRepository.save(Todo.of(request.getTitle(), request.getDescription(), request.getDueDate(), request.getPriority(), user));
        log.info("[TodoService createTodo] todo created by user: {}", user.getUsername());
        return TodoCreateResponse.fromEntity(todo);
    }

    @Transactional
    public TodoModifyResponse modifyTodo(TodoModifyRequest request,Long todoId, String username) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(ErrorCode.TODO_NOTFOUND, String.format("todo id: %s is not found", todoId)));
        if (!todo.getUser().getUsername().equals(username)) {
            throw new TodoException(ErrorCode.INVALUD_USER, String.format("%s is invalid",username));
        }

        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setDueDate(request.getDueDate());
        todo.setPriority(request.getPriority());
        todo.setCompleted(request.getCompleted());

        log.info("[TodoService modifyTodo] todo created by user: {}", username);

        return TodoModifyResponse.fromEntity(todo);
    }

    @Transactional
    public TodoDeleteResponse deleteTodo(Long todoId, String username){
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(ErrorCode.TODO_NOTFOUND, String.format("todo id: %s is not found", todoId)));
        if (!todo.getUser().getUsername().equals(username)) {
            throw new TodoException(ErrorCode.INVALUD_USER, String.format("%s is invalid", username));
        }

        todoRepository.delete(todo);

        log.info("[TodoService deleteTodo] todo deleted by user: {}", username);
        return TodoDeleteResponse.fromEntity(todo);
    }


}
