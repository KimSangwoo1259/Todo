package com.Todo.service;

import com.Todo.domain.Priority;
import com.Todo.domain.Todo;
import com.Todo.domain.User;
import com.Todo.dto.request.TodoCreateRequest;
import com.Todo.dto.request.TodoModifyRequest;
import com.Todo.dto.response.TodoCreateResponse;
import com.Todo.dto.response.TodoDeleteResponse;
import com.Todo.dto.response.TodoModifyResponse;
import com.Todo.dto.response.TodoResponse;
import com.Todo.exception.ErrorCode;
import com.Todo.exception.TodoException;
import com.Todo.repository.TodoRepository;
import com.Todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<TodoResponse> findAllTodo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TodoException(ErrorCode.USER_NOTFOUND, String.format("%s is not found", username)));

        log.info("[TodoService findAllTodo] findAll todo by user: {}", user.getUsername());


        return todoRepository.findAllByUser_Username(username).stream().map(TodoResponse::fromEntity).toList();
    }

    public TodoResponse findById(Long id, String username) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoException(ErrorCode.TODO_NOTFOUND, String.format("todo id: %s is not found", id)));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TodoException(ErrorCode.USER_NOTFOUND, String.format("%s is not found", username)));
        log.info("[TodoService findById] findbyid by user: {} id: {}", user.getUsername(),id);

        return TodoResponse.fromEntity(todo);
    }

    public List<TodoResponse> findTodoByUsernameOrderByDueDateAsc(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TodoException(ErrorCode.USER_NOTFOUND, String.format("%s is not found", username)));

        log.info("[TodoService findTodoByUsernameOrderByDueDateAs] by user: {}", user.getUsername());

        return todoRepository.findTodoByUsernameOrderByDueDateAsc(username).stream().map(TodoResponse::fromEntity).toList();
    }

    public List<TodoResponse> findTodoByUsernameAndPriority(String username, String priority) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TodoException(ErrorCode.USER_NOTFOUND, String.format("%s is not found", username)));

        log.info("[TodoService findTodoByUsernameAndPriority] by user: {}", user.getUsername());

        Priority p;
        try{
            p = Priority.valueOf(priority);
        } catch (IllegalArgumentException e){
            log.error("[TodoService findTodoByUsernameAndPriority] Invalid priority: {}", priority);
            throw new TodoException(ErrorCode.PRIORITY_NOT_FOUND, String.format("%s is not found", priority));
        }

        return todoRepository.findTodoByUsernameAndPriority(username,priority).stream().map(TodoResponse::fromEntity).toList();
    }

    public List<TodoResponse> findTodoByUsernameOrderByPriorityDesc(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TodoException(ErrorCode.USER_NOTFOUND, String.format("%s is not found", username)));

        log.info("[TodoService findTodoByUsernameOrderByPriorityDesc] by user: {}", user.getUsername());

        return todoRepository.findTodoByUsernameOrderByPriorityDesc(username).stream().map(TodoResponse::fromEntity).toList();
    }

    @Transactional
    public TodoModifyResponse modifyTodo(TodoModifyRequest request,Long todoId, String username) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(ErrorCode.TODO_NOTFOUND, String.format("todo id: %s is not found", todoId)));
        if (!todo.getUser().getUsername().equals(username)) {
            throw new TodoException(ErrorCode.INVALID_USER, String.format("%s is invalid",username));
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
            throw new TodoException(ErrorCode.INVALID_USER, String.format("%s is invalid", username));
        }

        todoRepository.delete(todo);

        log.info("[TodoService deleteTodo] todo deleted by user: {}", username);
        return TodoDeleteResponse.fromEntity(todo);
    }


}
