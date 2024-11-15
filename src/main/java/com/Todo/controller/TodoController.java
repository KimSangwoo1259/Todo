package com.Todo.controller;


import com.Todo.dto.Response;
import com.Todo.dto.request.TodoCreateRequest;
import com.Todo.dto.request.TodoModifyRequest;
import com.Todo.dto.response.TodoCreateResponse;
import com.Todo.dto.response.TodoDeleteResponse;
import com.Todo.dto.response.TodoModifyResponse;
import com.Todo.dto.response.TodoResponse;
import com.Todo.service.TodoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "todo api", description = "할일 관리 api")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public Response<TodoCreateResponse> createTodo(Authentication authentication, @RequestBody TodoCreateRequest request) {
        String username = authentication.getName();
        log.info("[TodoController create] try create todo username: {}", username);


        return Response.success(todoService.createTodo(request, username));
    }

    @GetMapping
    public Response<List<TodoResponse>> findAllTodos(Authentication authentication) {
        String username = authentication.getName();
        log.info("[TodoController findAllTodos] try get all todo username: {}", username);

        return Response.success(todoService.findAllTodo(username));
    }

    @GetMapping("/{todoId}")
    public Response<TodoResponse> findTodoById(Authentication authentication, @PathVariable Long todoId) {
        String username = authentication.getName();
        log.info("[TodoController findTodoById] try get find todo username: {}", username);
        return Response.success(todoService.findById(todoId, username));
    }

    @GetMapping("/dueDate")
    public Response<List<TodoResponse>> findTodosByDueDate(Authentication authentication) {
        String username = authentication.getName();

        log.info("[TodoController findTodosByDueDate] try get find todo username: {}", username);

        return Response.success(todoService.findTodoByUsernameOrderByDueDateAsc(username));

    }

    @GetMapping("/priority/{priority}")
    public Response<List<TodoResponse>> findTodoByUsernameAndPriority(Authentication authentication, @PathVariable String priority) {
        String username = authentication.getName();

        log.info("[TodoController findTodoByUsernameAndPriority] try get find todo username: {}", username);

        return Response.success(todoService.findTodoByUsernameAndPriority(username, priority));
    }

    @GetMapping("/priority")
    public Response<List<TodoResponse>> findTodoByUsernameOrderByPriorityDesc(Authentication authentication) {
        String username = authentication.getName();

        log.info("[TodoController findTodoByUsernameOrderByPriorityDesc] try get find todo username: {}", username);

        return Response.success(todoService.findTodoByUsernameOrderByPriorityDesc(username));
    }

    @PutMapping("/{todoId}")
    public Response<TodoModifyResponse> modifyTodo(Authentication authentication, @PathVariable Long todoId, @RequestBody TodoModifyRequest request) {
        String username = authentication.getName();

        log.info("[TodoController modify] try modify todo username: {}, todoId: {}", username, todoId);
        return Response.success(todoService.modifyTodo(request, todoId, username));
    }

    @DeleteMapping("/{todoId}")
    public Response<TodoDeleteResponse> deleteTodo(Authentication authentication, @PathVariable Long todoId) {
        String username = authentication.getName();

        log.info("[TodoController delete] try delete todo username: {}, todoId: {}", username, todoId);
        return Response.success(todoService.deleteTodo(todoId, username));
    }



}
