package hoon.community.controller.post;

import hoon.community.controller.response.Response;
import hoon.community.domain.post.dto.PostCreateRequest;
import hoon.community.domain.post.dto.PostReadCondition;
import hoon.community.domain.post.dto.PostUpdateRequest;
import hoon.community.domain.post.entity.BoardType;
import hoon.community.domain.post.service.PostService;
import hoon.community.global.aop.AssignMemberId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "Post Controller", tags = "Post")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @ApiOperation(value = "Create a post at FORUM", notes = "Create a post at FORUM board")
    @PostMapping("/forum")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createForum(@Valid @ModelAttribute PostCreateRequest request) {
        log.info("REQUEST = {}", request);
        return Response.success(postService.create(request, BoardType.FORUM));
    }

    @ApiOperation(value = "Create a post at NOTICE", notes = "Create a post at NOTICE board")
    @PostMapping("/notice")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createNotice(@Valid @ModelAttribute PostCreateRequest request) {
        return Response.success(postService.create(request, BoardType.NOTICE));
    }

    @ApiOperation(value = "Read a post", notes = "Read a post details")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@ApiParam(value = "Post id", required = true) @PathVariable Long id) {
        postService.updateHits(id);
        return Response.success(postService.read(id));
    }

    @ApiOperation(value = "Delete a post", notes = "Delete a post")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "Post id", required = true) @PathVariable Long id) {
        postService.delete(id);
        return Response.success();
    }


    @ApiOperation(value = "Update a post", notes = "Update a post")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response update(@ApiParam(value = "Post id", required = true) @PathVariable Long id, @Valid PostUpdateRequest request) {
        log.info("REQUEST UPDATE = {}", request);
        postService.update(id, request);
        return Response.success();
    }

    @ApiOperation(value = "Read all posts", notes = "Read all posts")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid @ModelAttribute PostReadCondition condition) {
        return Response.success(postService.readAll(condition));
    }
}
