package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    @Query(value = "select r from Recruit r where r.user = ?1 and r.id = ?2")
    Recruit findRecruit(User user, Long id);

    @Query(value = "select r from Recruit r where r.user = ?1 and r.category.id = ?2 and r.id = ?3")
    Recruit findRecruit(User user, Long categoryId, Long bookmarkId);

    @Query(value = "select r from Recruit r where r.user = ?1 and r.category.id = ?2")
    Page<Recruit> findRecruits(User user, Long categoryId, Pageable pageable);
}
