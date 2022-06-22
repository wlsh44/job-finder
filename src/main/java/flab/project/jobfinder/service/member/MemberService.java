package flab.project.jobfinder.service.member;

import flab.project.jobfinder.dto.member.Members;
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
    public Members save() {
        Members member = new Members();
        return memberRepository.save(member);
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.delete(
                memberRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id))
        );
    }

    public Members findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
