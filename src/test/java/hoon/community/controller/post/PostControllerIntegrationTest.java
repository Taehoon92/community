package hoon.community.controller.post;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.dto.PostCreateRequest;
import hoon.community.domain.post.entity.Post;
import hoon.community.domain.post.repository.PostRepository;
import hoon.community.domain.sign.dto.SignInResponse;
import hoon.community.domain.sign.service.SignService;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import hoon.community.global.factory.dto.PostCreateRequestFactory;
import hoon.community.global.factory.dto.SignInRequestFactory;
import hoon.community.global.factory.entity.PostFactory;
import hoon.community.global.init.TestInitDB;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static hoon.community.global.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerIntegrationTest {

    @Autowired WebApplicationContext context;
    @Autowired MockMvc mockMvc;

    @Autowired TestInitDB initDB;
    @Autowired MemberRepository memberRepository;
    @Autowired PostRepository postRepository;
    @Autowired SignService signService;

    Member member1, member2, admin;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        initDB.initDB();
        member1 = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        member2 = memberRepository.findByEmail(initDB.getMember2Email()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        admin = memberRepository.findByEmail(initDB.getAdminEmail()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Test
    void createTest() throws Exception {
        //given
        SignInResponse signInResponse = signService.signIn(SignInRequestFactory.createSignInRequest(member1.getEmail(), initDB.getPassword()));
        PostCreateRequest request = PostCreateRequestFactory.createPostCreateRequest("title", "content", member1.getId());

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/posts")
                                .param("title", request.getTitle())
                                .param("content", request.getContent())
                                .with(requestPostProcessor -> {
                                    requestPostProcessor.setMethod("POST");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("title");
        assertThat(post.getContent()).isEqualTo("content");
        assertThat(post.getMember().getId()).isEqualTo(member1.getId());

    }

    @Test
    void createUnauthorizedByNoneTokenTest() throws Exception {
        //given
        PostCreateRequest request = PostCreateRequestFactory.createPostCreateRequest("title", "content", member1.getId());

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/posts")
                                .param("title", request.getTitle())
                                .param("content", request.getContent())
                                .with(requestPostProcessor -> {
                                    requestPostProcessor.setMethod("POST");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/exception/entry-point"));
    }

    @Test
    void readTest() throws Exception {
        //given
        Post post = postRepository.save(createPost(member1));

        //when, then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/posts/{id}", post.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}