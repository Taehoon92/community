package hoon.community.controller.post;

import hoon.community.controller.response.Response;
import hoon.community.domain.post.dto.PostCreateRequest;
import hoon.community.domain.post.dto.PostUpdateRequest;
import hoon.community.domain.post.entity.BoardType;
import hoon.community.domain.post.service.PostService;
import hoon.community.global.aop.AssignMemberId;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostWebController {

    private final PostService postService;

    @GetMapping("/forum")
    public String listForum(Model model) {
        model.addAttribute("boardType",BoardType.FORUM);
        model.addAttribute("board","forum");
        return "post/list";
    }

    @GetMapping("/notice")
    public String listNotice(Model model) {
        model.addAttribute("boardType",BoardType.NOTICE);
        model.addAttribute("board","notice");
        return "post/list";
    }

    @GetMapping("/create/forum")
    public String createFormForum(Model model) {
        model.addAttribute("board","forum");
        return "post/create";
    }

    @GetMapping("/create/notice")
    public String createFormNotice(Model model) {
        model.addAttribute("board","notice");
        return "post/create";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable final Long id, Model model) {
        model.addAttribute("id",id);
        return "post/update";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable final Long id) {
        log.info("POST WEB CONTROLLER - VIEW");
        return "post/view";
    }
}
