package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    Recruit findByUserAndId(User user, Long id);
}
