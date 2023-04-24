package hoon.community.controller.exception;

import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExceptionControllerAdviceTest {

    @InjectMocks ExceptionController exceptionController;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(exceptionController).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

//    @Test
    void entryPointTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/exception/entry-point"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

//    @Test
    void accessDeniedTest() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/exception/access-denied"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}