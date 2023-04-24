package hoon.community.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import hoon.community.domain.comment.dto.CommentCreateRequest;
import hoon.community.domain.comment.service.CommentService;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import hoon.community.global.exception.GlobalExceptionHandler;
import hoon.community.global.factory.dto.CommentCreateRequestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static hoon.community.global.factory.dto.CommentCreateRequestFactory.createCommentCreateRequestWithMemberId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CommentControllerAdviceTest {
    @InjectMocks
    CommentController commentController;
    @Mock
    CommentService commentService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    //@Test
    void createExceptionByMemberNotFoundTest() throws Exception {
        //given
        BDDMockito.doThrow(new CustomException(ErrorCode.USER_NOT_FOUND)).when(commentService).create(any());
        CommentCreateRequest request = createCommentCreateRequestWithMemberId(null);

        //when, then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    //@Test
    void createExceptionByPostNotFoundTest() throws Exception {
        //given
        BDDMockito.doThrow(new CustomException(ErrorCode.POST_NOT_FOUND)).when(commentService).create(any());
        CommentCreateRequest request = createCommentCreateRequestWithMemberId(null);

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

    }

    //@Test
    void createExceptionByCommentNotFoundTest() throws Exception {
        //given
        BDDMockito.doThrow(new CustomException(ErrorCode.COMMENT_NOT_FOUND)).when(commentService).create(any());
        CommentCreateRequest request = createCommentCreateRequestWithMemberId(null);

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

    }

    //@Test
    void deleteExceptionByCommentNotFoundTest() throws Exception {
        //given
        BDDMockito.doThrow(new CustomException(ErrorCode.COMMENT_NOT_FOUND)).when(commentService).delete(anyLong());
        Long id = 1L;

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/comments/{id}", id))
                .andExpect(status().isNotFound());
    }
}