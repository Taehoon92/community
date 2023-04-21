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

    @ApiOperation(value = "Get comment lists", notes = "Get comment lists")
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
//    public Response readAll(@Valid @PathVariable CommentReadCondition condition) {
    public Response readAll(@Valid @PathVariable Long postId) {
        log.info("condition = {}", postId);
        return Response.success(commentService.readAll(postId));
    }

    @ApiOperation(value = "Create comment", notes = "Create comment")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(@Valid @RequestBody CommentCreateRequest request) {
        log.info("CommentCreateRequest = {}",request);
        commentService.create(request);
        return Response.success();
    }

    @ApiOperation(value = "Delete comment", notes = "Delete comment")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "Comment id", required = true) @PathVariable Long id) {
        commentService.delete(id);
        return Response.success();
    }
}
