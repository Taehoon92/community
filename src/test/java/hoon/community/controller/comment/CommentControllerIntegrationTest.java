package hoon.community.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import hoon.community.domain.comment.dto.CommentCreateRequest;
import hoon.community.domain.comment.dto.CommentDto;
import hoon.community.domain.comment.entity.Comment;
import hoon.community.domain.comment.repository.CommentRepository;
import hoon.community.domain.comment.service.CommentService;
import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.entity.Post;
import hoon.community.domain.post.repository.PostRepository;
import hoon.community.domain.sign.dto.SignInResponse;
import hoon.community.domain.sign.service.SignService;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import hoon.community.global.factory.dto.CommentCreateRequestFactory;
import hoon.community.global.factory.dto.CommentFactory;
import hoon.community.global.factory.dto.CommentReadConditionFactory;
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
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static hoon.community.global.factory.dto.CommentCreateRequestFactory.createCommentCreateRequest;
import static hoon.community.global.factory.dto.CommentFactory.createComment;
import static hoon.community.global.factory.dto.CommentReadConditionFactory.createCommentReadCondition;
import static hoon.community.global.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentControllerIntegrationTest {

    @Autowired WebApplicationContext context;
    @Autowired MockMvc mockMvc;

    @Autowired TestInitDB initDB;
    @Autowired MemberRepository memberRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired CommentService commentService;
    @Autowired PostRepository postRepository;
    @Autowired SignService signService;
    ObjectMapper objectMapper = new ObjectMapper();

    Member member1, member2, admin;
    Post post;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        initDB.initDB();

        member1 = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        member2 = memberRepository.findByEmail(initDB.getMember2Email()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        admin = memberRepository.findByEmail(initDB.getAdminEmail()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        post = postRepository.save(PostFactory.createPost(member1));
    }

    @Test
    void readAllTest() throws Exception {
        //given, when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/comments")
                                .param("postId", String.valueOf(1)))

                .andExpect(status().isOk());
    }

    @Test
    void createTest() throws Exception {
        //given
        CommentCreateRequest request = createCommentCreateRequest("content", post.getId(), null, null);
        SignInResponse signInResponse = signService.signIn(createSignInRequest(member1.getEmail(), initDB.getPassword()));

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/comments")
                                .header("Authorization", signInResponse.getAccessToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        List<CommentDto> result = commentService.readAll(createCommentReadCondition(post.getId()));
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void createUnauthorizedByNoneTokenTest() throws Exception {
        //given
        CommentCreateRequest request = createCommentCreateRequest("content", post.getId(), member1.getId(), null);

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void deleteByResourceOwnerTest() throws Exception {
        //given
        Comment comment = commentRepository.save(createComment(member1, post, null));
        SignInResponse signInResponse = signService.signIn(createSignInRequest(member1.getEmail(), initDB.getPassword()));

        //when, then
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/comments/{id}", comment.getId())
                        .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().isOk());

        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteByAdminTest() throws Exception {
        //given
        Comment comment = commentRepository.save(createComment(member1, post, null));
        SignInResponse signInResponse = signService.signIn(createSignInRequest(admin.getEmail(), initDB.getPassword()));

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/comments/{id}", comment.getId())
                                .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().isOk());

        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteUnauthorizedByNoneTokenTest() throws Exception {
        //given
        Comment comment = commentRepository.save(createComment(member1, post, null));

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/comments/{id}", comment.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));

    }

    @Test
    void deleteAccessDeniedByNotResourceOwnerTest() throws Exception {
        //given
        Comment comment = commentRepository.save(createComment(member1, post, null));
        SignInResponse signInResponse = signService.signIn(createSignInRequest(member2.getEmail(), initDB.getPassword()));

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/comments/{id}", comment.getId())
                                .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }

}