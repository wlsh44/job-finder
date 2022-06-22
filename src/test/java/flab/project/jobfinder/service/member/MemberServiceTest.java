package flab.project.jobfinder.service.member;

import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.dto.member.Member;
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

    SignUpFormDto signUpFormDto;
    Member member;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
        String name = "test";
        String password = "password";
        String email = "email@email.email";
        signUpFormDto = SignUpFormDto.builder()
                .name(name)
                .password(password)
                .passwordConfirm(password)
                .email(email)
                .build();
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

    @Test
    @DisplayName("조회 테스트")
    void findByIdTest() {
        //given
        given(memberRepository.save(any())).willReturn(member);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        Long saveMemberId = memberService.save(signUpFormDto);

        //when
        Member res = memberService.findById(saveMemberId);

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
        given(memberRepository.save(any())).willReturn(member);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        Long saveMemberId = memberService.save(signUpFormDto);

        System.out.println("saveMemberId = " + saveMemberId);
        //when
        memberService.delete(saveMemberId);

        //then
        assertThatThrownBy(() -> memberService.findById(saveMemberId))
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