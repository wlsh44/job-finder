package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.RecruitTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruitTagRepository extends JpaRepository<RecruitTag, Long> {
    boolean existsByRecruit_IdAndTag_Name(Long recruitId, String tagName);
    Optional<RecruitTag> findByRecruit_IdAndTag_Id(Long recruitId, Long tagId);
    int countByTag_Id(Long tagId);
}
