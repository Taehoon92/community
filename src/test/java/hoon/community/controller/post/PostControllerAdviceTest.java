package hoon.community.controller.post;

import hoon.community.domain.post.dto.PostCreateRequest;
import hoon.community.domain.post.service.PostService;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.GlobalExceptionHandler;
import hoon.community.global.factory.dto.PostCreateRequestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class PostControllerAdviceTest {

    @InjectMocks PostController postController;
    @Mock PostService postService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).setControllerAdvice(new GlobalExceptionHandler()).build();
    }
/*
    @Test
    void createExceptionByMemberNotFoundException() throws Exception {
        //given
        given(postService.create(ArgumentMatchers.any())).willThrow(CustomException.class);

        //when, then
        performCreate()
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }



    @Test
    void readExceptionByPostNotFoundTest() throws Exception {
        //given
        given(postService.read(ArgumentMatchers.anyLong())).willThrow(CustomException.class);

        //when, then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/posts/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteExceptionByPostNotFoundTest() throws Exception {
        //given
        doThrow(CustomException.class).when(postService).delete(ArgumentMatchers.anyLong());

        //when, then
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/posts/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }



    @Test
    void updateExceptionByPostNotFoundTest() throws Exception{
        //given
        doThrow(CustomException.class).when(postService).update(anyLong(), any());

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/posts/{id}", 1L)
                                .param("title", "title")
                                .param("content", "content")
                                .with(requestPostProcessor -> {
                                    requestPostProcessor.setMethod("PATCH");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
*/
    private ResultActions performCreate() throws Exception {
        PostCreateRequest request = PostCreateRequestFactory.createPostCreateRequest();
        return mockMvc.perform(
                MockMvcRequestBuilders.multipart("/api/posts")
                        .param("title", request.getTitle())
                        .param("content", request.getContent())
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA));
    }




}