package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    long removeById(Long id);
}
