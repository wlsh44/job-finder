package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.RecruitTag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecruitTagRepository extends JpaRepository<RecruitTag, Long> {
    boolean existsByRecruit_IdAndTag_Name(Long recruitId, String tagName);
    boolean existsByRecruit_IdAndTag_Id(Long recruitId, Long tagId);
    void deleteByRecruit_idAndTag_Id(Long recruitId, Long tagId);
}
