package hoon.community.controller.member;

import hoon.community.domain.member.service.MemberService;
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
class MemberControllerTest {

    @InjectMocks MemberController memberController;
    @Mock MemberService memberService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    void readTest() throws Exception {
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/members/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());
        BDDMockito.verify(memberService).read(id);
    }

    @Test
    void deleteTest() throws Exception {
        //given
        Long id = 1L;

        //when,then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/members/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());
        BDDMockito.verify(memberService).delete(id);
    }
}