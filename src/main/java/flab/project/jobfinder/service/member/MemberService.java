package flab.project.jobfinder.service.member;

import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.dto.member.Member;
import flab.project.jobfinder.exception.member.LoginFailedException;
import flab.project.jobfinder.exception.member.SignUpFailedException;
import flab.project.jobfinder.exception.member.UserNotFoundException;
import flab.project.jobfinder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member login(LoginFormDto loginFormDto) {
        Member member = memberRepository.findByName(loginFormDto.getName())
                .orElseThrow(LoginFailedException::new);
        if (!member.getPassword().equals(loginFormDto.getPassword())) {
            throw new LoginFailedException();
        }
        return member;
    }

    @Transactional
    public Long save(SignUpFormDto signUpFormDto) {
        if (memberRepository.existsByName(signUpFormDto.getName())) {
            throw new SignUpFailedException("이미 존재하는 유저");
        }
        if (!signUpFormDto.getPassword().equals(signUpFormDto.getPasswordConfirm())) {
            throw new SignUpFailedException("비밀번호 검증 실패");
        }
        //TODO
        //비밀번호 암호화
        Member member = Member.builder()
                .name(signUpFormDto.getName())
                .password(signUpFormDto.getPassword())
                .email(signUpFormDto.getEmail())
                .build();
        return memberRepository.save(member).getId();
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.delete(
                memberRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id))
        );
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
