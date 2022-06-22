package flab.project.jobfinder.service.member;

import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.dto.member.Member;
import flab.project.jobfinder.exception.member.LoginFailedException;
import flab.project.jobfinder.exception.member.SignUpFailedException;
import flab.project.jobfinder.exception.member.UserNotFoundException;
import flab.project.jobfinder.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    Member member;
    String name = "test";
    String password = "password";
    String email = "email@email.email";


    @BeforeEach
    void clean() {
        memberRepository.deleteAll();

        member = Member.builder()
                .id(1L)
                .name(name)
                .password(password)
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
            given(memberRepository.save(any())).willReturn(member);

            //when
            Long res = memberService.save(signUpFormDto);

            //then
            assertThat(res).isEqualTo(1L);
        }

        @Test
        @DisplayName("이미 존재하는 유저인 경우")
        void alreadyExistsMember() {
            //given
            given(memberRepository.existsByName("test")).willReturn(true);

            //when then
            assertThatThrownBy(() -> memberService.save(signUpFormDto))
                    .isInstanceOf(SignUpFailedException.class)
                    .hasMessage("회원가입에 실패했습니다: 이미 존재하는 유저");
        }

        @Test
        @DisplayName("이미 존재하는 유저인 경우")
        void passwordConfirmFailed() {
            //given
            signUpFormDto = SignUpFormDto.builder()
                    .name("test")
                    .password("password1")
                    .passwordConfirm("wrong password")
                    .email("asdf@asdf.asdf")
                    .build();
            given(memberRepository.existsByName("test")).willReturn(false);

            //when then
            assertThatThrownBy(() -> memberService.save(signUpFormDto))
                    .isInstanceOf(SignUpFailedException.class)
                    .hasMessage("회원가입에 실패했습니다: 비밀번호 검증 실패");
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {

        LoginFormDto loginFormDto;

        @BeforeEach
        void init() {
            loginFormDto = LoginFormDto.builder().name(name)
                    .password(password)
                    .build();
        }

        @Test
        @DisplayName("로그인 성공")
        void loginTest() {
            //given
            given(memberRepository.findByName(name)).willReturn(Optional.of(member));

            //when
            Member res = memberService.login(loginFormDto);

            //then
            assertThat(res).isEqualTo(member);
        }

        @Test
        @DisplayName("없는 유저일 경우")
        void notExistsMember() {
            //given
            given(memberRepository.findByName(name)).willReturn(Optional.empty());

            //when then
            assertThatThrownBy(() -> memberService.login(loginFormDto))
                    .isInstanceOf(LoginFailedException.class)
                    .hasMessage("로그인에 실패했습니다: 존재하지 않는 유저");
        }

        @Test
        @DisplayName("비밀번호 틀릴 경우")
        void notCorrectPassword() {
            //given
            given(memberRepository.findByName(name)).willReturn(Optional.of(member));
            loginFormDto = LoginFormDto.builder()
                                    .name(name)
                                    .password("wrong passwrod")
                                    .build();

            //when then
            assertThatThrownBy(() -> memberService.login(loginFormDto))
                    .isInstanceOf(LoginFailedException.class)
                    .hasMessage("로그인에 실패했습니다: 비밀번호 틀림");
        }
    }

    @Test
    @DisplayName("조회 테스트")
    void findByIdTest() {
        //given
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        //when
        Member res = memberService.findById(1L);

        //then
        assertThat(res.getName()).isEqualTo(member.getName());
        assertThat(res.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("조회 실패 테스트")
    void findByIdFailTest() {
        //given
        Long id = -1L;

        //when then
        assertThatThrownBy(() -> memberService.findById(id))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("삭제 테스트")
    @Disabled(value = "삭제 테스트 어떻게 할까")
    void deleteTest() {
        //given
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        //when
        memberService.delete(1L);

        //then
        assertThatThrownBy(() -> memberService.findById(1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("삭제 실패 테스트")
    void deleteFailTest() {
        //given
        Long id = -1L;

        //when then
        assertThatThrownBy(() -> memberService.delete(id))
                .isInstanceOf(UserNotFoundException.class);
    }
}