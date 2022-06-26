package flab.project.jobfinder.controller;

import flab.project.jobfinder.config.UserPropertiesConfig;
import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.repository.UserRepository;
import flab.project.jobfinder.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserPropertiesConfig propertiesConfig;


    @Nested
    @DisplayName("회원가입")
    class SignUpTest {

        @Test
        @DisplayName("회원가입 GET")
        void signUpGET_test() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/user/sign-up"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("회원가입 POST 성공")
        void signUpPOST_test() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/user/sign-up")
                            .param("email", "test@test.test")
                            .param("password", "test")
                            .param("passwordConfirm", "test")
                            .param("name", "test"))
                    .andDo(print())
                    .andExpect(model().errorCount(0))
                    .andExpect(status().is3xxRedirection());
        }

        @Test
        @DisplayName("회원가입 POST 이메일 형식 안맞음")
        void signUpPOST_InvalidEmail_test() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/user/sign-up")
                            .param("email", "a@a.a")
                            .param("password", "test")
                            .param("passwordConfirm", "test")
                            .param("name", "test"))
                    .andDo(print())
                    .andExpect(model().errorCount(1))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("회원가입 POST 존재하는 이메일")
        void signUpPOST_existsEmail_test() throws Exception {
            //given
            given(userRepository.existsByEmail("test@test.test")).willReturn(true);

            //when then
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/user/sign-up")
                            .param("email", "test@test.test")
                            .param("password", "test")
                            .param("passwordConfirm", "test")
                            .param("name", "test"))
                    .andDo(print())
                    .andExpect(model().errorCount(1))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("회원가입 POST 비밀번호 안 맞을 때")
        void signUpPOST_passwordCheckFail_test() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/user/sign-up")
                            .param("email", "test@test.test")
                            .param("password", "test1")
                            .param("passwordConfirm", "test2")
                            .param("name", "test"))
                    .andDo(print())
                    .andExpect(model().errorCount(1))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("회원가입 POST 이름 형식 안 맞을 떄")
        void signUpPOST_invalidNamePattern_test() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/user/sign-up")
                            .param("email", "test@test.test")
                            .param("password", "test")
                            .param("passwordConfirm", "test")
                            .param("name", "tes"))
                    .andDo(print())
                    .andExpect(model().errorCount(1))
                    .andExpect(status().isOk());
        }
    }
}