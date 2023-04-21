package hoon.community.controller.post;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.dto.PostCreateRequest;
import hoon.community.domain.post.dto.PostReadCondition;
import hoon.community.domain.post.entity.Post;
import hoon.community.domain.post.repository.PostRepository;
import hoon.community.domain.post.service.PostService;
import hoon.community.domain.sign.dto.SignInResponse;
import hoon.community.domain.sign.service.SignService;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import hoon.community.global.factory.dto.PostCreateRequestFactory;
import hoon.community.global.init.TestInitDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static hoon.community.global.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static hoon.community.global.factory.dto.SignInRequestFactory.createSignInRequest;
import static hoon.community.global.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @Autowired PostService postService;

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
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        SignInResponse signInResponse = signService.signIn(createSignInRequest(member1.getEmail(), initDB.getPassword()),servletResponse);
        PostCreateRequest request = PostCreateRequestFactory.createPostCreateRequest("title", "content", member1.getId(), List.of());

        //when, then
        mockMvc.perform(
                        multipart("/api/posts")
                                .param("title", request.getTitle())
                                .param("content", request.getContent())
                                .with(requestPostProcessor -> {
                                    requestPostProcessor.setMethod("POST");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().isCreated());

        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("title");
        assertThat(post.getContent()).isEqualTo("content");
        assertThat(post.getMember().getId()).isEqualTo(member1.getId());

    }

    @Test
    void createUnauthorizedByNoneTokenTest() throws Exception {
        //given
        PostCreateRequest request = PostCreateRequestFactory.createPostCreateRequest("title", "content", member1.getId(), List.of());

        //when, then
        mockMvc.perform(
                        multipart("/api/posts")
                                .param("title", request.getTitle())
                                .param("content", request.getContent())
                                .with(requestPostProcessor -> {
                                    requestPostProcessor.setMethod("POST");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/exception/entry-point"));
    }

    @Test
    void readTest() throws Exception {
        //given
        Post post = postRepository.save(createPost(member1));

        //when, then
        mockMvc.perform(
                get("/api/posts/{id}", post.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByResourceOwnerTest() throws Exception {
        //given
        Post post = postRepository.save(createPost(member1));
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        SignInResponse signInResponse = signService.signIn(createSignInRequest(member1.getEmail(), initDB.getPassword()), servletResponse);

        //when, then
        mockMvc.perform(
                delete("/api/posts/{id}", post.getId())
                        .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().isOk());

        assertThatThrownBy(() -> postService.read(post.getId())).isInstanceOf(CustomException.class);
    }

    @Test
    void deleteByAdminTest() throws Exception {
        //given
        Post post = postRepository.save(createPost(member1));
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        SignInResponse signInResponse = signService.signIn(createSignInRequest(admin.getEmail(), initDB.getPassword()), servletResponse);

        //when, then
        mockMvc.perform(
                delete("/api/posts/{id}", post.getId())
                        .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().isOk());

        assertThatThrownBy(() -> postService.read(post.getId())).isInstanceOf(CustomException.class);
    }

    @Test
    void deleteAccessDeniedByNotResourceOwnerTest() throws Exception {
        //given
        Post post = postRepository.save(createPost(member1));
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        SignInResponse signInResponse = signService.signIn(createSignInRequest(member2.getEmail(), initDB.getPassword()), servletResponse);

        //when, then
        mockMvc.perform(
                        delete("/api/posts/{id}", post.getId())
                                .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }

    @Test
    void deleteUnauthorizedByNoneTokenTest() throws Exception {
        //given
        Post post = postRepository.save(createPost(member1));
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        SignInResponse signInResponse = signService.signIn(createSignInRequest(member2.getEmail(), initDB.getPassword()), servletResponse);

        //when, then
        mockMvc.perform(
                        delete("/api/posts/{id}", post.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void updateByResourceOwnerTest() throws Exception {
        //given
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        SignInResponse signInResponse = signService.signIn(createSignInRequest(member1.getEmail(), initDB.getPassword()), servletResponse);
        Post post = postRepository.save(createPost(member1));
        String updatedTitle = "updated title";
        String updatedContent = "updated content";

        //when, then
        mockMvc.perform(
                        multipart("/api/posts/{id}", post.getId())
                                .param("title", updatedTitle)
                                .param("content", updatedContent)
                                .with(requestPostProcessor -> {
                                    requestPostProcessor.setMethod("PATCH");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA)
//                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().isOk());

        Post updatedPost = postRepository.findById(post.getId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        assertThat(updatedPost.getTitle()).isEqualTo(updatedTitle);
        assertThat(updatedPost.getContent()).isEqualTo(updatedContent);
    }

    @Test
    void updateUnauthorizedByNoneTokenTest() throws Exception {
        //given
        Post post = postRepository.save(createPost(member1));
        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";

        //when, then
        mockMvc.perform(
                        multipart("/api/posts/{id}", post.getId())
                                .param("title", updatedTitle)
                                .param("content", updatedContent)
                                .with(requestPostProcessor -> {
                                    requestPostProcessor.setMethod("PATCH");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void updateAccessDeniedByNotResourceOwnerTest() throws Exception {
        //given
        Post post = postRepository.save(createPost(member1));
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        SignInResponse response = signService.signIn(createSignInRequest(member2.getEmail(), initDB.getPassword()), servletResponse);
        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";

        //when,then
        mockMvc.perform(
                        multipart("/api/posts/{id}", post.getId())
                                .param("title", updatedTitle)
                                .param("content", updatedContent)
                                .with(requestPostProcessor -> {
                                    requestPostProcessor.setMethod("PATCH");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .header("Authorization", response.getAccessToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));


    }

    @Test
    void readAllTest() throws Exception {
        //given
        PostReadCondition condition = createPostReadCondition(0, 1);

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/posts")
                                .param("page", String.valueOf(condition.getPage())).param("size", String.valueOf(condition.getSize()))
                                .param("memberId", String.valueOf(1L), String.valueOf(2L)))
                .andExpect(status().isOk());
    }
}