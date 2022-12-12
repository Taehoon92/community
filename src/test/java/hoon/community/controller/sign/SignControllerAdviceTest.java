package hoon.community.controller.sign;

import com.fasterxml.jackson.databind.ObjectMapper;
import hoon.community.domain.sign.dto.SignInRequest;
import hoon.community.domain.sign.service.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class SignControllerAdviceTest {

    @InjectMocks SignController signController;
    @Mock SignService signService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
//        mockMvc = MockMvcBuilders.standaloneSetup(signController).setControllerAdvice(new ExceptionAdvice()).build();
    }

    @Test
    void signInLoginFailureExceptionTest() throws Exception {
        //given
        SignInRequest request = new SignInRequest("test@email.com", "password1!");
//        BDDMockito.given(signService.signIn(ArgumentMatchers.any())).willThrow(LoginFailu)
        //when

        //then
    }

}
