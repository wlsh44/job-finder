package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.user.LoginFailedException;
import flab.project.jobfinder.exception.user.SignUpFailedException;
import flab.project.jobfinder.exception.user.UserNotFoundException;
import flab.project.jobfinder.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    User user;
    String name = "test";
    String password = "password";
    String email = "email@email.email";
    String encodedPassword;

    @BeforeEach
    void init() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        encodedPassword = encoder.encode(password);

        user = User.builder()
                .id(1L)
                .name(name)
                .password(encodedPassword)
                .email(email)
                .build();
    }

    @Nested
    @DisplayName("저장 테스트")
    class SaveTest {

        SignUpFormDto signUpFormDto;

        @BeforeEach
        void init() {
            signUpFormDto = SignUpFormDto.builder()
                    .name(name)
                    .password(password)
                    .passwordConfirm(password)
                    .email(email)
                    .build();
        }

        @Test
        @DisplayName("저장 성공")
        void saveTest() {
            //given
            given(userRepository.save(any()))
                    .willReturn(user);

            //when
            Long res = userService.save(signUpFormDto);

            //then
            assertThat(res).isEqualTo(1L);
        }

        @Test
        @DisplayName("이미 존재하는 유저인 경우")
        void alreadyExistsMember() {
            //given
            given(userRepository.existsByEmail(email))
                    .willReturn(true);

            //when then
            assertThatThrownBy(() -> userService.save(signUpFormDto))
                    .isInstanceOf(SignUpFailedException.class)
                    .hasMessage("회원가입에 실패: 이미 존재하는 유저입니다.");
        }

        @Test
        @DisplayName("이미 존재하는 유저인 경우")
        void passwordConfirmFailed() {
            //given
            signUpFormDto = SignUpFormDto.builder()
                    .name(name)
                    .password(password)
                    .passwordConfirm("wrong password")
                    .email(email)
                    .build();
            given(userRepository.existsByEmail(email)).willReturn(false);

            //when then
            assertThatThrownBy(() -> userService.save(signUpFormDto))
                    .isInstanceOf(SignUpFailedException.class)
                    .hasMessage("회원가입에 실패: 비밀번호가 일치하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {

        LoginFormDto loginFormDto;

        @BeforeEach
        void init() {
            loginFormDto = LoginFormDto.builder().email(email)
                    .password(password)
                    .build();
        }

        @Test
        @DisplayName("로그인 성공")
        void loginTest() {
            //given
            given(userRepository.findByEmail(email))
                    .willReturn(Optional.of(user));
            given(passwordEncoder.matches(loginFormDto.getPassword(), user.getPassword()))
                    .willReturn(true);

            //when
            User res = userService.login(loginFormDto);

            //then
            assertThat(res).isEqualTo(user);
        }

        @Test
        @DisplayName("없는 유저일 경우")
        void notExistsMember() {
            //given
            given(userRepository.findByEmail(email))
                    .willReturn(Optional.empty());

            //when then
            assertThatThrownBy(() -> userService.login(loginFormDto))
                    .isInstanceOf(LoginFailedException.class)
                    .hasMessage("로그인에 실패했습니다: 존재하지 않는 유저");
        }

        @Test
        @DisplayName("비밀번호 틀릴 경우")
        void notCorrectPassword() {
            //given
            given(userRepository.findByEmail(email))
                    .willReturn(Optional.of(user));
            loginFormDto = LoginFormDto.builder()
                                    .email(email)
                                    .password("wrong passwrod")
                                    .build();

            //when then
            assertThatThrownBy(() -> userService.login(loginFormDto))
                    .isInstanceOf(LoginFailedException.class)
                    .hasMessage("로그인에 실패했습니다: 비밀번호 틀림");
        }
    }

    @Test
    @DisplayName("조회 테스트")
    void findByIdTest() {
        //given
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        //when
        User res = userService.findById(1L);

        //then
        assertThat(res.getName()).isEqualTo(user.getName());
        assertThat(res.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("조회 실패 테스트")
    void findByIdFailTest() {
        //given
        Long id = -1L;

        //when then
        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("삭제 테스트")
    @Disabled(value = "삭제 테스트 어떻게 할까")
    void deleteTest() {
        //given
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        //when
        userService.delete(1L);

        //then
        assertThatThrownBy(() -> userService.findById(1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("삭제 실패 테스트")
    void deleteFailTest() {
        //given
        Long id = -1L;

        //when then
        assertThatThrownBy(() -> userService.delete(id))
                .isInstanceOf(UserNotFoundException.class);
    }
}