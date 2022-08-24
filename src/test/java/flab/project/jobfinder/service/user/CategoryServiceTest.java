package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.CategoryResponseDto;
import flab.project.jobfinder.dto.bookmark.NewCategoryRequestDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.CategoryException;
import flab.project.jobfinder.repository.CategoryRepository;
import flab.project.jobfinder.repository.RecruitTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static flab.project.jobfinder.enums.bookmark.CategoryResponseCode.FAILED_CREATE_CATEGORY;
import static flab.project.jobfinder.enums.bookmark.CategoryResponseCode.FAILED_DELETE_CATEGORY;
import static flab.project.jobfinder.enums.exception.CategoryErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    User user;
    Category category1;
    Category category2;
    Category category3;
    List<Category> categoryList;

    @BeforeEach
    void init() {
        user = User.builder()
                .email("test@test.test")
                .name("test")
                .password("test")
                .build();
        category1 = Category.builder()
                .id(1L)
                .user(user)
                .recruits(new ArrayList<>())
                .name("category1").build();
        category2 = Category.builder()
                .id(2L)
                .user(user)
                .recruits(new ArrayList<>())
                .name("category2").build();
        category3 = Category.builder()
                .id(3L)
                .user(user)
                .recruits(new ArrayList<>())
                .name("category3").build();
        categoryList = List.of(category1, category2, category3);
    }

    @Test
    @DisplayName("전체 카테고리 조회")
    void findCategoriesByUserTest() {
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
        List<Category> categoryList = List.of(category1, category2, category3);
        given(categoryRepository.findByUser(user))
                .willReturn(categoryList);

        //when
        List<CategoryResponseDto> categoryDto = categoryService.findAllByUser(user);

        //then
        assertThat(categoryDto.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("카테고리 생성")
    void createCategoryTest() {
        //given
        String categoryName = "category";
        NewCategoryRequestDto dto = new NewCategoryRequestDto();
        dto.setName(categoryName);
        Category category = Category.builder()
                .id(1L)
                .user(user)
                .name(categoryName)
                .build();
        given(categoryRepository.countByUser(user))
                .willReturn(0);
        given(categoryRepository.existsByUserAndName(user, dto.getName()))
                .willReturn(false);
        given(categoryRepository.save(any()))
                .willReturn(category);

        //when
        CategoryResponseDto categoryDto = categoryService.create(user, dto);

        //then
        assertThat(categoryDto.getId()).isEqualTo(1L);
        assertThat(categoryDto.getName()).isEqualTo(categoryName);
    }

    @Test
    @DisplayName("이미 있는 카테고리 생성할 경우")
    void createCategoryTest_AlreadyExistCategoryName_Fail() {
        //given
        String categoryName = "category";
        NewCategoryRequestDto dto = new NewCategoryRequestDto();
        dto.setName(categoryName);
        given(categoryRepository.countByUser(user))
                .willReturn(0);
        given(categoryRepository.existsByUserAndName(user, dto.getName()))
                .willReturn(true);

        //when then
        assertThatThrownBy(() -> categoryService.create(user, dto))
                .isInstanceOf(CategoryException.class)
                .hasMessage(new CategoryException(FAILED_CREATE_CATEGORY, ALREADY_EXISTS_CATEGORY).getMessage());
    }


    @Test
    @DisplayName("최대 카테고리 수 초과할 경우")
    void createCategoryTest_Fail() {
        //given
        String categoryName = "category";
        NewCategoryRequestDto dto = new NewCategoryRequestDto();
        dto.setName(categoryName);
        given(categoryRepository.countByUser(user))
                .willReturn(11);

        //when then
        assertThatThrownBy(() -> categoryService.create(user, dto))
                .isInstanceOf(CategoryException.class)
                .hasMessage(new CategoryException(FAILED_CREATE_CATEGORY, TOO_MANY_CATEGORIES).getMessage());
    }

    @Test
    @DisplayName("리스트에 포함된 카테고리 조회")
    void findByNameInTest() {
        //given
        List<String> categoryNameList = List.of("category1", "category2");
        List<Category> expect = List.of(this.category1, category2);
        given(categoryRepository.findByNameIn(categoryNameList))
                .willReturn(List.of(category1, category2));

        //when
        List<Category> res = categoryService.findByNameIn(categoryNameList);

        //then
        assertThat(res).isEqualTo(expect);
    }

    @Test
    @DisplayName("카테고리 아이디 존재할 경우")
    void existsByUserAndId_True() {
        //given
        Long categoryId = 1L;
        given(categoryRepository.existsByUserAndId(user, categoryId))
                .willReturn(true);

        //when
        boolean res = categoryService.existsByUserAndId(user, categoryId);

        //then
        assertThat(res).isTrue();
    }

    @Test
    @DisplayName("카테고리 아이디 존재x 경우")
    void existsByUserAndId_False() {
        //given
        Long categoryId = 1L;
        given(categoryRepository.existsByUserAndId(user, categoryId))
                .willReturn(false);

        //when
        boolean res = categoryService.existsByUserAndId(user, categoryId);

        //then
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("카테고리 아이디로 조회")
    void findByUserAndId() {
        //given
        Long categoryId = 1L;
        given(categoryRepository.findCategory(user, categoryId))
                .willReturn(Optional.of(category1));

        //when
        Optional<Category> category = categoryService.findByUserAndId(user, categoryId);

        //then
        assertThat(category.get()).isEqualTo(category1);
    }

    @Test
    @DisplayName("카테고리 아이디로 조회 - 없을 경우")
    void findByUserAndId_Fail() {
        //given
        Long categoryId = 1L;
        given(categoryRepository.findCategory(user, categoryId))
                .willReturn(Optional.empty());

        //when
        Optional<Category> category = categoryService.findByUserAndId(user, categoryId);

        //then
        assertThat(category.isEmpty()).isTrue();
    }
}