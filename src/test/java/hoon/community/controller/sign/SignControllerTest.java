package hoon.community.controller.sign;

import com.fasterxml.jackson.databind.ObjectMapper;
import hoon.community.domain.sign.dto.SignInRequest;
import hoon.community.domain.sign.dto.SignInResponse;
import hoon.community.domain.sign.dto.SignUpRequest;
import hoon.community.domain.sign.service.SignService;
import hoon.community.global.factory.dto.RefreshTokenResponseFactory;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SignControllerTest {

    @InjectMocks SignController signController;
    @Mock SignService signService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(signController).build();
    }

    @Test
    void signUpTest() throws Exception {
        //given
        SignUpRequest request = new SignUpRequest("loginId", "password1!", "username", "test@email.com");

        //when, then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        BDDMockito.verify(signService).signUp(request);
    }

    @Test
    void signInTest() throws Exception {
        //given
        SignInRequest request = new SignInRequest("test@email.com", "password1!");
        BDDMockito.given(signService.signIn(request)).willReturn(new SignInResponse("access", "refresh"));

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.accessToken").value("access"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.refreshToken").value("refresh"));

        BDDMockito.verify(signService).signIn(request);
    }

    @Test
    void ignoreNullValueInJsonResponseTest() throws Exception {
        //given
        SignUpRequest request = new SignUpRequest("loginId", "password1!", "username", "email@email.com");

        //when,then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").doesNotExist());
    }

    @Test
    void refreshTokenTest() throws  Exception {
        //given
        BDDMockito.given(signService.refreshToken("refreshToken")).willReturn(RefreshTokenResponseFactory.createRefreshTokenResponse("accessToken"));

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/refresh-token")
                                .header("Authorization", "refreshToken"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.accessToken").value("accessToken"));
    }
}