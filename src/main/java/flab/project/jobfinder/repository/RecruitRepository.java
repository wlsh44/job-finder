package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    Recruit findByUserAndId(User user, Long id);
    Recruit findByUserAndCategory_IdAndId(User user, Long categoryId, Long bookmarkId);
    Page<Recruit> findByUserAndCategory_Id(User user, Long categoryId, Pageable pageable);
}
