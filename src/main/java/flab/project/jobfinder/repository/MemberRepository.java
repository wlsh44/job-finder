package flab.project.jobfinder.repository;

import flab.project.jobfinder.dto.member.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {
    Optional<Members> findById(Long id);
}
