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

    @ApiOperation(value = "FORUM 게시글 생성", notes = "게시글을 생성한다")
    @PostMapping("/forum")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createForum(@Valid @ModelAttribute PostCreateRequest request) {
        log.info("REQUEST = {}", request);
        return Response.success(postService.create(request, BoardType.FORUM));
    }

    @ApiOperation(value = "FORUM 게시글 생성", notes = "게시글을 생성한다")
    @PostMapping("/notice")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createNotice(@Valid @ModelAttribute PostCreateRequest request) {
        return Response.success(postService.create(request, BoardType.NOTICE));
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
    public Response update(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id, @Valid /*@RequestBody*/ PostUpdateRequest request) {
        log.info("REQUEST UPDATE = {}", request);
        postService.update(id, request);
        return Response.success();
    }

    @ApiOperation(value = "게시판 게시글 목록 조회", notes = "게시글 목록을 조회한다.")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid @ModelAttribute PostReadCondition condition) {
        return Response.success(postService.readAll(condition));
    }

    @ApiOperation(value = "NOTICE 게시판 게시글 목록 조회", notes = "NOTICE 게시글 목록을 조회한다.")
    @GetMapping("/notice")
    @ResponseStatus(HttpStatus.OK)
    public Response readAllNotice(@Valid @ModelAttribute PostReadCondition condition) {
        condition.setBoardType(List.of(BoardType.NOTICE));
        return Response.success(postService.readAll(condition));
    }

    @ApiOperation(value = "FORUM 게시판 게시글 목록 조회", notes = "FORUM 게시글 목록을 조회한다.")
    @GetMapping("/forum")
    @ResponseStatus(HttpStatus.OK)
    public Response readAllForum(@Valid @ModelAttribute PostReadCondition condition) {
        condition.setBoardType(List.of(BoardType.FORUM));
        return Response.success(postService.readAll(condition));
    }

    /*

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
    public Response update(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id, @Valid PostUpdateRequest request) {
        log.info("REQUEST UPDATE = {}", request);
        postService.update(id, request);
        return Response.success();
    }

    @ApiOperation(value = "게시글 목록 조회", notes = "게시글 목록을 조회한다.")
    @GetMapping("/notice")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid @ModelAttribute PostReadCondition condition) {
        return Response.success(postService.readAll(condition));
    }

    */

}
