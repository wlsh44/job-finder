package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUser(User user);
    Optional<Category> findByUserAndId(User user, Long id);
    boolean existsByUserAndName(User user, String name);
    boolean existsByUserAndId(User user, Long id);
}
