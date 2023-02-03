package hoon.community.controller.member;

import hoon.community.controller.response.Response;
import hoon.community.domain.member.dto.MemberPasswordModifyDto;
import hoon.community.domain.member.dto.MemberReadCondition;
import hoon.community.domain.member.service.MemberService;
import hoon.community.global.aop.AssignMemberId;
import hoon.community.global.security.guard.AuthHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.print.Pageable;

@Api(value = "Member Controller", tags = "Member")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "사용자 정보 조회", notes = "사용자 정보를 조회한다.")
    @GetMapping("/api/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(memberService.read(id));
    }

    @GetMapping("/api/members")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid @ModelAttribute MemberReadCondition condition) {
        return Response.success(memberService.readAll(condition));
    }


    @ApiOperation(value = "사용자 정보 삭제", notes = "사용자 정보를 삭제한다.")
    @DeleteMapping("/api/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable Long id) {
        memberService.delete(id);
        return Response.success();
    }

    @ApiOperation(value = "로그인 사용자 정보 조회", notes = "로그인된 사용자 정보를 조회한다.")
    @GetMapping("/api/members/details")
    @ResponseStatus(HttpStatus.OK)
    public Response details() {
        return Response.success(memberService.memberDetails(AuthHelper.extractMemberId()));
    }

    @ApiOperation(value = "사용자 비밀번호 변경", notes = "사용자의 비밀번호를 변경한다.")
    @PostMapping("/api/members/modify/password")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId
    public Response modifyPassword(@Valid @ModelAttribute("dto") MemberPasswordModifyDto request, BindingResult bindingResult) {
        memberService.modifyPassword(request, bindingResult);
        return Response.success();
    }
}
