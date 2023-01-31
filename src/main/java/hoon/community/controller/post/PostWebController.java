package hoon.community.controller.post;

import hoon.community.controller.response.Response;
import hoon.community.domain.post.dto.PostCreateRequest;
import hoon.community.domain.post.dto.PostUpdateRequest;
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

    @GetMapping("")
    public String list() {
        return "post/list";
    }

    @GetMapping("/create")
    public String createForm() {
        return "post/create";
    }

    @PostMapping("/create")
    @AssignMemberId
    public String create(@Valid @ModelAttribute PostCreateRequest request) {
        postService.create(request);

        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable final Long id, Model model) {
        model.addAttribute("id",id);
        return "post/update";
    }


    @GetMapping("/{id}")
    public String view(@PathVariable final Long id) {

        return "post/view";
    }
}
