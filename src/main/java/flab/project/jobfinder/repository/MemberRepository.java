package flab.project.jobfinder.repository;

import flab.project.jobfinder.dto.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    boolean existsByName(String name);
}
