package hoon.community.controller.comment;

import hoon.community.controller.response.Response;
import hoon.community.domain.comment.dto.CommentCreateRequest;
import hoon.community.domain.comment.dto.CommentReadCondition;
import hoon.community.domain.comment.service.CommentService;
import hoon.community.global.aop.AssignMemberId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Comment Controller", tags = "Comment")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 목록 조회", notes = "댓글 목록을 조회한다.")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid CommentReadCondition condition) {
        return Response.success(commentService.readAll(condition));
    }

    @ApiOperation(value = "댓글 생성", notes = "댓글을 생성한다")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(@Valid @RequestBody CommentCreateRequest request) {
        commentService.create(request);
        return Response.success();
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제한다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "댓글 id", required = true) @PathVariable Long id) {
        commentService.delete(id);
        return Response.success();
    }
}
