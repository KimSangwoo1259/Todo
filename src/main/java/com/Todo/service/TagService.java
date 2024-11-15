package com.Todo.service;

import com.Todo.domain.Tag;
import com.Todo.domain.Todo;
import com.Todo.domain.User;
import com.Todo.dto.request.TagCreateRequest;
import com.Todo.dto.request.TagModifyRequest;
import com.Todo.dto.response.TagCreateResponse;
import com.Todo.dto.response.TagDeleteResponse;
import com.Todo.dto.response.TagModifyResponse;
import com.Todo.exception.ErrorCode;
import com.Todo.exception.TodoException;
import com.Todo.repository.TagRepository;
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
public class TagService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final TagRepository tagRepository;

    @Transactional
    public TagCreateResponse createTag(TagCreateRequest request,Long todoId, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TodoException(ErrorCode.USER_NOTFOUND, String.format("%s is not found", username)));
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(ErrorCode.TODO_NOTFOUND, String.format("%s is not found", todoId)));
        log.info("[TagService createTag] tag created by user: {}, tagId: {}", username, todoId);

        Tag savedTag = tagRepository.save(Tag.of(request.getName(), todo));

        return TagCreateResponse.fromEntity(savedTag, todo);

    }


    @Transactional
    public TagModifyResponse modifyTag(TagModifyRequest request, Long todoId, Long tagId, String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TodoException(ErrorCode.USER_NOTFOUND, String.format("%s is not found", username)));
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(ErrorCode.TODO_NOTFOUND, String.format("%s is not found", todoId)));
        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            throw new TodoException(ErrorCode.INVALID_USER, String.format("%s is invalid", user.getUsername()));
        }

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TodoException(ErrorCode.TAG_NOT_FOUND, String.format("%s is not found", tagId)));

        tag.setName(request.getChangingName());

        log.info("[TagService modifyTag] tag modified by user: {}, tagId: {}", username, tagId);
        return TagModifyResponse.fromEntity(tag, todo);
    }

    @Transactional
    public TagDeleteResponse deleteTag(Long todoId, Long tagId, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TodoException(ErrorCode.USER_NOTFOUND, String.format("%s is not found", username)));
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(ErrorCode.TODO_NOTFOUND, String.format("%s is not found", todoId)));
        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            throw new TodoException(ErrorCode.INVALID_USER, String.format("%s is invalid", user.getUsername()));
        }

        log.info("[TagService deleteTag] tag deleted by user: {}, tagId: {}", username, todoId);

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TodoException(ErrorCode.TAG_NOT_FOUND, String.format("%s is not found", tagId)));

        tagRepository.deleteById(tagId);

        return TagDeleteResponse.fromEntity(tag);
    }
}
