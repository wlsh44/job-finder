package flab.project.jobfinder.service.member;

import flab.project.jobfinder.dto.member.Member;
import flab.project.jobfinder.exception.UserNotFoundException;
import flab.project.jobfinder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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
