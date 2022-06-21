package flab.project.jobfinder.repository;

import flab.project.jobfinder.dto.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
