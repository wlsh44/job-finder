package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.service.user.CategoryService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    User user;
    Category saveCategory;
    String categoryName;

    @BeforeEach
    void init() {
        user = User.builder()
                .name("test1")
                .password("password")
                .email("test1@test.test")
                .categories(new ArrayList<>())
                .recruits(new ArrayList<>())
                .build();
        user = userRepository.save(user);
        categoryName = "testCategory";
        Category category = Category.builder()
                .user(user)
                .name(categoryName).build();
        saveCategory = categoryRepository.save(category);
    }

    @Test
    @DisplayName("전체 카테고리 조회 테스트")
    void findByUserTest() {
        //given
        Category category1 = Category.builder()
                .user(user)
                .name("testCategory1")
                .recruits(new ArrayList<>()).build();
        Category category2 = Category.builder()
                .user(user)
                .name("testCategory2")
                .recruits(new ArrayList<>()).build();
        Category category3 = Category.builder()
                .user(user)
                .name("testCategory3")
                .recruits(new ArrayList<>()).build();
        List<Category> categoryList = List.of(saveCategory, category1, category2, category3);
        categoryRepository.saveAll(categoryList);

        //when
        List<Category> categories = categoryRepository.findByUser(user);

        //then
        assertThat(categories).isEqualTo(categoryList);
    }

    @Test
    @DisplayName("카테고리 다중 조회 테스트")
    void findByNameInTest() {
        //given
        Category category1 = Category.builder()
                .user(user)
                .name("testCategory1")
                .recruits(new ArrayList<>()).build();
        Category category2 = Category.builder()
                .user(user)
                .name("testCategory2")
                .recruits(new ArrayList<>()).build();
        Category category3 = Category.builder()
                .user(user)
                .name("testCategory3")
                .recruits(new ArrayList<>()).build();
        List<Category> categoryList = List.of(category1, category2, category3);
        categoryRepository.saveAll(categoryList);
        List<String> categoryNameList = List.of("testCategory1", "testCategory2", "testCategory3");

        //when
        List<Category> categories = categoryRepository.findByNameIn(categoryNameList);

        //then
        assertThat(categories).isEqualTo(categoryList);
    }

    @Test
    @DisplayName("특정 카테고리 조회 테스트 - id")
    void findCategoryTest() {
        //given

        //when
        Category res = categoryRepository.findCategory(user, saveCategory.getId()).get();

        //then
        assertThat(res.getName()).isEqualTo(categoryName);
        assertThat(res.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("특정 카테고리 조회 테스트 - 이름")
    void findByUserAndNameTest() {
        //given

        //when
        Category res = categoryRepository.findCategory(user, categoryName).get();

        //then
        assertThat(res.getName()).isEqualTo(categoryName);
        assertThat(res.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("특정 카테고리 조회 체크 테스트 - 이름")
    void existsByUserAndNameTest() {
        //given

        //when
        boolean res = categoryRepository.existsByUserAndName(user, categoryName);

        //then
        assertThat(res).isTrue();
    }

    @Test
    @DisplayName("특정 카테고리 조회 체크 테스트 - id")
    void existsByUserAndIdTest() {
        //given

        //when
        boolean res = categoryRepository.existsByUserAndId(user, saveCategory.getId());

        //then
        assertThat(res).isTrue();
    }

    @Test
    @DisplayName("카테고리 수 확인")
    void countByUser() {
        //given

        //when
        int categoryNum = categoryRepository.countByUser(user);

        //given
        assertThat(categoryNum).isEqualTo(1);
    }
}