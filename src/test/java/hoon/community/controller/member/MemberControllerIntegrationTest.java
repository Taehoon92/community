package hoon.community.controller.member;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.sign.dto.SignInRequest;
import hoon.community.domain.sign.dto.SignInResponse;
import hoon.community.domain.sign.service.SignService;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import hoon.community.global.init.TestInitDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerIntegrationTest {

    @Autowired WebApplicationContext context;
    @Autowired MockMvc mockMvc;

    @Autowired TestInitDB initDB;
    @Autowired SignService signService;
    @Autowired MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
        initDB.initDB();
    }

    @Test
    void readTest() throws Exception {
        //given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/members/{id}", member.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteTest() throws Exception {
        //given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        SignInResponse signInResponse = signService.signIn(new SignInRequest(initDB.getMember1Email(), initDB.getPassword()));

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/members/{id}", member.getId()).header("Authorization", signInResponse.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void deleteByAdminTest() throws Exception {
        //given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        SignInResponse signInResponse = signService.signIn(new SignInRequest(initDB.getAdminEmail(), initDB.getPassword()));

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/members/{id}", member.getId()).header("Authorization", signInResponse.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void deleteUnauthorizedByNoneTokenTest() throws Exception {
        //given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/members/{id}", member.getId()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/exception/entry-point"));
    }

    @Test
    void deleteAccessDeniedByNotResourceOwnerTest() throws Exception {
        //given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        SignInResponse signInResponse = signService.signIn(new SignInRequest(initDB.getMember2Email(), initDB.getPassword()));

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/members/{id}", member.getId()).header("Authorization", signInResponse.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/exception/access-denied"));
    }

    @Test
    void deleteUnauthorizedByRefreshTokenTest() throws Exception {
        //given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        SignInResponse signInResponse = signService.signIn(new SignInRequest(initDB.getMember1Email(), initDB.getPassword()));

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/members/{id}", member.getId()).header("Authorization", signInResponse.getRefreshToken()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/exception/entry-point"));
    }

}