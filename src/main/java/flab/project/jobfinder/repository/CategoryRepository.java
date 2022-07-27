package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);

    List<Category> findByNameIn(List<String> categoryNameList);

    @Query(value = "select c from Category c where c.user = ?1 and c.id = ?2")
    Optional<Category> findCategory(User user, Long id);

    @Query(value = "select c from Category c where c.user = ?1 and c.name = ?2")
    Optional<Category> findCategory(User user, String name);

    boolean existsByUserAndName(User user, String name);

    boolean existsByUserAndId(User user, Long id);

    int countByUser(User user);
}
