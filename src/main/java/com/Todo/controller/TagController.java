package com.Todo.controller;


import com.Todo.dto.Response;
import com.Todo.dto.request.TagCreateRequest;
import com.Todo.dto.request.TagModifyRequest;
import com.Todo.dto.response.TagCreateResponse;
import com.Todo.dto.response.TagDeleteResponse;
import com.Todo.dto.response.TagModifyResponse;
import com.Todo.service.TagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "tag api", description = "Todo의 Tag관리 api")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class TagController {

    private final TagService tagService;

    @PostMapping("/todo/{todoId}/tag")
    public Response<TagCreateResponse> createTag(@RequestBody TagCreateRequest tagCreateRequest,@PathVariable Long todoId, Authentication authentication) {
        String username = authentication.getName();
        log.info("[TagController create] try create tag username: {}", username);

        return Response.success(tagService.createTag(tagCreateRequest,todoId, username));
    }

    @PutMapping("/todo/{todoId}/tag/{tagId}")
    public Response<TagModifyResponse> modifyTag(@RequestBody TagModifyRequest tagModifyRequest, @PathVariable Long todoId, @PathVariable Long tagId, Authentication authentication) {
        String username = authentication.getName();
        log.info("[TagController modify] try modify tag username: {}, todoId: {}, tagId: {}", username, todoId, tagId);
        return Response.success(tagService.modifyTag(tagModifyRequest,todoId,tagId, username));
    }

    @DeleteMapping("/todo/{todoId}/tag/{tagId}")
    public Response<TagDeleteResponse> deleteTag(@PathVariable Long todoId, @PathVariable Long tagId, Authentication authentication) {
        String username = authentication.getName();
        log.info("[TagController delete] try delete tag username: {}, todoId: {}, tagId: {}", username, todoId, tagId);
        return Response.success(tagService.deleteTag(todoId,tagId,username));
    }
}
