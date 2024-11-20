package com.Todo.service;

import com.Todo.domain.Priority;
import com.Todo.domain.Todo;
import com.Todo.domain.User;
import com.Todo.dto.request.TagCreateRequest;
import com.Todo.dto.request.TodoCreateRequest;
import com.Todo.dto.request.TodoModifyRequest;
import com.Todo.dto.response.TagCreateResponse;
import com.Todo.dto.response.TodoCreateResponse;
import com.Todo.dto.response.TodoModifyResponse;
import com.Todo.exception.ErrorCode;
import com.Todo.exception.TodoException;
import com.Todo.repository.TodoRepository;
import com.Todo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("TodoList")
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TagService tagService;

    @InjectMocks private TodoService sut;

    @DisplayName("Todo를 만들면 ID를 반환한다.")
    @Test
    void givenTodoInformation_whenCreatingTodo_thenReturnTodoId() {

        //given
        TodoCreateRequest request = createRequest();

        User mockUser = createUser(); // 가짜 사용자 객체
        Todo mockTodo = createTodo(request, mockUser); // ID 주입

        // Mocking
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo);


        // when
        TodoCreateResponse response = sut.createTodo(request, "testUser");

        // then
        Assertions.assertThat(response.getTodoId()).isEqualTo(1L); // 반환된 ID 확인
        verify(todoRepository).save(any(Todo.class));// save 메서드 호출 확인
    }

    @DisplayName("Todo를 만들때 User가 존재하지 않으면 예외를 반환한다.")
    @Test
    void givenNonExistUser_whenCreatingTodo_thenReturnException() {

        //given
        String wrongUsername = "wrongUser";
        TodoCreateRequest request = createRequest();
        given(userRepository.findByUsername(wrongUsername)).willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> sut.createTodo(request, wrongUsername));

        // then
        assertThat(t)
                .isInstanceOf(TodoException.class);
        then(userRepository).should().findByUsername(wrongUsername);
    }

    @DisplayName("사용자가 Todo를 조회하면 본인의 Todo List를 반환한다")
    @Test
    void givenUserName_WhenFindTodoList_thenReturnTodoList() {
        //given
        User mockUser = createUser();
        String username = mockUser.getUsername();
        Todo mockTodo = createTodo(createRequest(), mockUser);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(mockUser));
        given(todoRepository.findAllByUser_Username(username)).willReturn(List.of(mockTodo));

        //when
        sut.findAllTodo(mockUser.getUsername());

        //then
        then(todoRepository).should().findAllByUser_Username(username);
        assertThat(todoRepository.findAllByUser_Username(username)).hasSize(1);
    }

    @DisplayName("TodoId로 Todo를 조회하면 Todo를 반환한다.")
    @Test
    void givenTodoId_WhenFindTodo_thenReturnTodo() {
        //given
        User mockUser = createUser();
        String username = mockUser.getUsername();
        Todo mockTodo = createTodo(createRequest(), mockUser);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(mockUser));
        given(todoRepository.findById(mockTodo.getId())).willReturn(Optional.of(mockTodo));

        //when
        sut.findById(mockTodo.getId(),username);

        //then
        then(todoRepository).should().findById(mockTodo.getId());
        assertThat(todoRepository.findById(1L)).isEqualTo(Optional.of(mockTodo));
    }

    @DisplayName("존재하지 않는 TodoId를 조회하면 예외를 반환한다.")
    @Test
    void givenNonExistTodoId_WhenFindTodo_thenThrowException() {
        //given
        Long nonExistTodoId = 2L;


        given(todoRepository.findById(nonExistTodoId)).willReturn(Optional.empty());
        //when
        Throwable t = catchThrowable(() -> sut.findById(nonExistTodoId, anyString()));
        //then
        assertThat(t).isInstanceOf(TodoException.class);
        then(todoRepository).should().findById(nonExistTodoId);
    }

    @DisplayName("Todo를 변경하면 변경된 todo를 반환한다.")
    @Test
    void givenTodoModifyRequest_whenModifyTodo_thenReturnModifiedTodoId() {
        //given
        User mockUser = createUser();
        Todo originTodo = createTodo(createRequest(), mockUser);
        Long modifyingTodoId = originTodo.getId();
        String username = mockUser.getUsername();
        TodoModifyRequest request = new TodoModifyRequest("modifiedTitle", "modDes", LocalDateTime.now().plusDays(4), Priority.LOW, true);

        given(todoRepository.findById(modifyingTodoId)).willReturn(Optional.of(originTodo));

        //when
        TodoModifyResponse response = sut.modifyTodo(request, modifyingTodoId, username);

        //then
        assertThat(response)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("title", "modifiedTitle")
                .hasFieldOrPropertyWithValue("description", "modDes")
                .hasFieldOrPropertyWithValue("priority", Priority.LOW)
                .hasFieldOrPropertyWithValue("completed", true);
        then(todoRepository).should().findById(modifyingTodoId);


    }



    private Todo createTodo(TodoCreateRequest request, User user) {
        Todo todo = Todo.of(
                request.getTitle(),
                request.getDescription(),
                request.getDueDate(),
                request.getPriority(),
                user
        );
        ReflectionTestUtils.setField(todo, "id", 1L); // ID 주입
        return todo;
    }

    private User createUser(){
        User user = User.of("testUser", "1234", "email@mail.com");
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private TodoCreateRequest createRequest() {
        return new TodoCreateRequest(
                "title",
                "des",
                LocalDateTime.now().plusDays(3),
                Priority.MEDIUM,
                List.of("tag1", "tag2"));
    }

}