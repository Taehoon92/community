package hoon.community.controller.post;

import hoon.community.controller.response.Response;
import hoon.community.domain.post.dto.PostCreateRequest;
import hoon.community.domain.post.dto.PostReadCondition;
import hoon.community.domain.post.dto.PostUpdateRequest;
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

@Api(value = "Post Controller", tags = "Post")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @ApiOperation(value = "게시글 생성", notes = "게시글을 생성한다")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(@Valid @ModelAttribute PostCreateRequest request) {
        return Response.success(postService.create(request));
    }

    @ApiOperation(value = "게시글 조회", notes = "게시글을 조회한다")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id) {
        postService.updateHits(id);
        return Response.success(postService.read(id));
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제한다")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id) {
        postService.delete(id);
        return Response.success();
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정한다.")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response update(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id, @Valid @RequestBody PostUpdateRequest request) {
        postService.update(id, request);
        return Response.success();
    }

    @ApiOperation(value = "게시글 목록 조회", notes = "게시글 목록을 조회한다.")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid @ModelAttribute PostReadCondition condition) {
        return Response.success(postService.readAll(condition));
    }
}
