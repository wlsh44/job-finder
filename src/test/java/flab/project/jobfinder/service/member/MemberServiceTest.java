package flab.project.jobfinder.service.member;

import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.dto.member.Member;
import flab.project.jobfinder.exception.member.UserNotFoundException;
import flab.project.jobfinder.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
                .passwordCheck(password)
                .email(email)
                .build();
        member = Member.builder()
                .id(1L)
                .name(name)
                .password(password)
                .email(email)
                .build();
    }

    @Test
    @DisplayName("저장 테스트")
    void saveTest() {
        //given
        given(memberRepository.save(any())).willReturn(member);

        //when
        Long res = memberService.save(signUpFormDto);

        //then
        assertThat(res).isEqualTo(1L);
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