package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.user.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeAll
    void init() {
        userRepository.deleteAll();
        user = User.builder()
                .name("test")
                .password("password")
                .email("test@test.test")
                .build();
        user = userRepository.save(user);
    }

    @BeforeEach
    void clean() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("전체 카테고리 조회 테스트")
    void findAllByUserTest() {
        //given
        Category category1 = Category.builder()
                .user(user)
                .name("category1").build();
        Category category2 = Category.builder()
                .user(user)
                .name("category2").build();
        Category category3 = Category.builder()
                .user(user)
                .name("category3").build();
        categoryRepository.saveAll(List.of(category1, category2, category3));

        //when
        List<Category> categories = categoryRepository.findAllByUser(user);

        //then
        assertThat(categories.size()).isEqualTo(3);
        assertThat(categories.contains(category1)).isTrue();
    }

    @Test
    @DisplayName("특정 카테고리 조회 테스트")
    void findCategoryByUserAndNameTest() {
        //given
        String categoryName = "category";
        Category category = Category.builder()
                .user(user)
                .name(categoryName).build();
        categoryRepository.save(category);

        //when
        Category res = categoryRepository.findCategoryByUserAndName(user, categoryName).get();

        //then
        assertThat(res.getName()).isEqualTo(categoryName);
        assertThat(res.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("특정 카테고리 조회 체크 테스트")
    void existsByUserAndNameTest() {
        //given
        String categoryName = "category";
        Category category = Category.builder()
                .user(user)
                .name(categoryName).build();
        categoryRepository.save(category);

        //when
        boolean res = categoryRepository.existsByUserAndName(user, categoryName);

        //then
        assertThat(res).isTrue();
    }
}