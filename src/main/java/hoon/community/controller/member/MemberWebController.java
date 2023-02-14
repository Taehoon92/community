package hoon.community.controller.member;

import hoon.community.controller.response.Response;
import hoon.community.domain.member.dto.MemberPasswordModifyDto;
import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.service.MemberService;
import hoon.community.domain.role.dto.RoleModifyDto;
import hoon.community.domain.role.entity.RoleType;
import hoon.community.global.aop.AssignMemberId;
import hoon.community.global.security.guard.AuthHelper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberWebController {

    private final MemberService memberService;

    @GetMapping("/details")
    public String details() {
        return "member/details";
    }

    @GetMapping("/list")
    public String lists(@ModelAttribute RoleModifyDto dto) {
        return "member/list";
    }

    @GetMapping("/modify/password")
    public String modifyPassword(@ModelAttribute("dto") MemberPasswordModifyDto request) {
        return "member/modifyPassword";
    }

    @ApiOperation(value = "사용자 비밀번호 변경", notes = "사용자의 비밀번호를 변경한다.")
    @PostMapping("/modify/password")
    @AssignMemberId
    public String password(@Valid @ModelAttribute("dto") MemberPasswordModifyDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "member/modifyPassword";
        }

        boolean result = memberService.modifyPassword(request, bindingResult);

        if(!result) {
            return "member/modifyPassword";
        }

        return "redirect:/members/details";
    }

    @PostMapping("/modify/roles/{id}")
    public String modifyRoles(@PathVariable Long id, @ModelAttribute("roles") RoleModifyDto dto) {

        memberService.modifyRoles(id, dto);

        return "redirect:/members/list";
    }

}
