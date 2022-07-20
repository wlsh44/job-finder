package flab.project.jobfinder.controller;

import flab.project.jobfinder.config.UserPropertiesConfig;
import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.enums.exception.LoginFailedErrorCode;
import flab.project.jobfinder.exception.user.LoginFailedException;
import flab.project.jobfinder.exception.user.SignUpFailedException;
import flab.project.jobfinder.repository.UserRepository;
import flab.project.jobfinder.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static flab.project.jobfinder.enums.exception.SignUpFailedErrorCode.ALREADY_EXISTS_USER;
import static flab.project.jobfinder.enums.exception.SignUpFailedErrorCode.PASSWORD_CONFIRM_NOT_CORRECT;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
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

        String signUpUrl = "/sign-up";

        @Test
        @DisplayName("회원가입 GET")
        void signUpGET_test() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .get(signUpUrl))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("회원가입 POST 성공")
        void signUpPOST_test() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .post(signUpUrl)
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
                            .post(signUpUrl)
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
            SignUpFormDto dto = SignUpFormDto.builder()
                    .email("test@test.test")
                    .name("test")
                    .password("test")
                    .passwordConfirm("test")
                    .build();
            given(userService.save(dto)).willThrow(new SignUpFailedException(dto, ALREADY_EXISTS_USER));

            //when then
            mockMvc.perform(MockMvcRequestBuilders
                            .post(signUpUrl)
                            .param("email", "test@test.test")
                            .param("password", "test")
                            .param("passwordConfirm", "test")
                            .param("name", "test"))
                    .andDo(print())
                    .andExpect(model().attributeExists("emailError"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("회원가입 POST 비밀번호 안 맞을 때")
        void signUpPOST_passwordCheckFail_test() throws Exception {
            SignUpFormDto dto = SignUpFormDto.builder()
                    .email("test@test.test")
                    .name("test")
                    .password("test1")
                    .passwordConfirm("test2")
                    .build();
            given(userService.save(dto)).willThrow(new SignUpFailedException(dto, PASSWORD_CONFIRM_NOT_CORRECT));

            mockMvc.perform(MockMvcRequestBuilders
                            .post(signUpUrl)
                            .param("email", "test@test.test")
                            .param("password", "test1")
                            .param("passwordConfirm", "test2")
                            .param("name", "test"))
                    .andDo(print())
                    .andExpect(model().attributeExists("passwordError"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("회원가입 POST 이름 형식 안 맞을 떄")
        void signUpPOST_invalidNamePattern_test() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .post(signUpUrl)
                            .param("email", "test@test.test")
                            .param("password", "test")
                            .param("passwordConfirm", "test")
                            .param("name", "tes"))
                    .andDo(print())
                    .andExpect(model().errorCount(1))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("로그인")
    class LoginTest {

        String url = "/login";

        @Test
        @DisplayName("로그인 GET")
        void loginGET_test() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .get(url))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("로그인 POST 성공")
        void loginPOST_test() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .param("email", "test@test.test")
                            .param("password", "test"))
                    .andDo(print())
                    .andExpect(model().errorCount(0))
                    .andExpect(status().is3xxRedirection());
        }

        @Test
        @DisplayName("로그인 POST 이메일 형식 안맞음")
        void loginPOST_InvalidEmail_test() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .param("email", "a@a.a")
                            .param("password", "test"))
                    .andDo(print())
                    .andExpect(model().errorCount(1))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("로그인 POST 존재하지 않는 이메일")
        void loginPOST_existsEmail_test() throws Exception {
            //given
            LoginFormDto dto = LoginFormDto.builder()
                    .email("test@test.test")
                    .password("test")
                    .build();
            given(userService.login(dto)).willThrow(new LoginFailedException(dto, LoginFailedErrorCode.NOT_EXISTS_USER));

            //when then
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .param("email", "test@test.test")
                            .param("password", "test"))
                    .andDo(print())
                    .andExpect(model().attributeExists("loginFailed"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("로그인 POST 비밀번호 안 맞을 때")
        void loginPOST_passwordCheckFail_test() throws Exception {
            //given
            LoginFormDto dto = LoginFormDto.builder()
                    .email("test@test.test")
                    .password("test")
                    .build();
            given(userService.login(dto)).willThrow(new LoginFailedException(dto, LoginFailedErrorCode.WRONG_PASSWORD));

            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .param("email", "test@test.test")
                            .param("password", "test"))
                    .andDo(print())
                    .andExpect(model().attributeExists("loginFailed"))
                    .andExpect(status().isOk());
        }
    }
}