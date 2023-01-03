package hoon.community.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import hoon.community.domain.comment.dto.CommentCreateRequest;
import hoon.community.domain.comment.dto.CommentReadCondition;
import hoon.community.domain.comment.service.CommentService;
import hoon.community.global.factory.dto.CommentCreateRequestFactory;
import hoon.community.global.factory.dto.CommentReadConditionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static hoon.community.global.factory.dto.CommentCreateRequestFactory.createCommentCreateRequestWithMemberId;
import static hoon.community.global.factory.dto.CommentReadConditionFactory.createCommentReadCondition;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @InjectMocks
    CommentController commentController;
    @Mock
    CommentService commentService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    void readAllTest() throws Exception {
        //given
        CommentReadCondition condition = createCommentReadCondition();

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/comments/")
                                .param("postId", String.valueOf(condition.getPostId())))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(commentService).readAll(condition);

    }

    @Test
    void createTest() throws  Exception {
        //given
        CommentCreateRequest request = createCommentCreateRequestWithMemberId(null);
        //when, then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(commentService).create(request);
    }

    @Test
    void deleteTest() throws Exception {
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/comments/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(commentService).delete(id);

    }
}