package hoon.community.controller.post;

import hoon.community.domain.post.dto.PostCreateRequest;
import hoon.community.domain.post.service.PostService;
import hoon.community.global.factory.dto.PostCreateRequestFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @InjectMocks PostController postController;
    @Mock PostService postService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    void createTest() throws Exception {
        //given
        ArgumentCaptor<PostCreateRequest> postCreateRequestArgumentCaptor = ArgumentCaptor.forClass(PostCreateRequest.class);

        PostCreateRequest request = PostCreateRequestFactory.createPostCreateRequest();

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
                .andExpect(MockMvcResultMatchers.status().isCreated());

        BDDMockito.verify(postService).create(postCreateRequestArgumentCaptor.capture());

    }

    @Test
    void readTest() throws Exception {
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/posts/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BDDMockito.verify(postService).read(id);
    }

}