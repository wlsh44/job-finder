package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.recruit.RecruitTag;
import flab.project.jobfinder.entity.recruit.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruitTagRepository extends JpaRepository<RecruitTag, Long> {
    Optional<RecruitTag> findByRecruitAndTag(Recruit recruit, Tag tag);
}
