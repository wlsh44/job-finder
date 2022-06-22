package flab.project.jobfinder.service.member;

import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.dto.member.Member;
import flab.project.jobfinder.exception.LoginFailedException;
import flab.project.jobfinder.exception.UserNotFoundException;
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
                .orElseThrow(() -> new UserNotFoundException(loginFormDto.getName()));
        if (!member.getPassword().equals(loginFormDto.getPassword())) {
            throw new LoginFailedException();
        }
        return member;
    }

    @Transactional
    public Long save(Member memberDto) {
        Member member = Member.builder()
                .name(memberDto.getName())
                .password(memberDto.getPassword())
                .email(memberDto.getEmail())
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
