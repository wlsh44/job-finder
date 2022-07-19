package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.Tag;
import flab.project.jobfinder.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByNameAndUser(String name, User user);
    List<Tag> findTagsByUser(User user);
    Optional<Tag> findByUserAndName(User user, String name);
}
