package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.RecruitTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecruitTagRepository extends JpaRepository<RecruitTag, Long> {
    @Query(value = "select * from Recruit_Tag as rt " +
            "left outer join Recruit r on rt.recruit_id = r.id " +
            "left outer join Tag t on rt.tag_id = t.id " +
            "where r.id = ?1 and t.name = ?2 limit 1", nativeQuery = true)
    boolean existsRecruitTag(Long recruitId, String tagName);

    @Query(value = "select rt from RecruitTag rt where rt.recruit.id = ?1 and rt.tag.id = ?2")
    Optional<RecruitTag> findRecruitTag(Long recruitId, Long tagId);

    @Query(value = "select count(rt) from RecruitTag rt where rt.tag.id = ?1")
    int countRecruitTag(Long tagId);
}
