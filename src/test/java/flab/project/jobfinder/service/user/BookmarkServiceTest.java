package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.CategoryResponseDto;
import flab.project.jobfinder.dto.bookmark.DeleteCategoryRequestDto;
import flab.project.jobfinder.dto.bookmark.NewCategoryRequestDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.CategoryNotFoundException;
import flab.project.jobfinder.exception.bookmark.CreateCategoryFailedException;
import flab.project.jobfinder.repository.CategoryRepository;
import flab.project.jobfinder.repository.RecruitRepository;
import flab.project.jobfinder.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static flab.project.jobfinder.enums.exception.CreateCategoryFailedErrorCode.ALREADY_EXISTS_CATEGORY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @InjectMocks
    BookmarkService bookmarkService;

    @Mock
    RecruitRepository recruitRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UserRepository userRepository;

    @Nested
    @DisplayName("카테고리 테스트")
    class CategoryTest {

        User user;

        @BeforeEach
        void init() {
            user = User.builder()
                    .email("test@test.test")
                    .name("test")
                    .password("test")
                    .build();
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
            given(categoryRepository.findAllByUser(user)).willReturn(categoryList);
            given(userRepository.existsById(user.getId())).willReturn(true);

            //when
            List<CategoryResponseDto> categoryResponseDto = bookmarkService.findCategoriesByUser(user);

            //then
            assertThat(categoryResponseDto.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("카테고리 생성")
        void createCategoryTest() {
            //given
            String categoryName = "category";
            NewCategoryRequestDto dto = new NewCategoryRequestDto(categoryName);
            Category category = Category.builder()
                    .id(1L)
                    .user(user)
                    .name(categoryName)
                    .build();
            given(categoryRepository.existsByUserAndName(user, dto.getName())).willReturn(false);
            given(categoryRepository.save(any())).willReturn(category);

            //when
            CategoryResponseDto responseDto = bookmarkService.createCategory(user, dto);

            //then
            assertThat(responseDto.getId()).isEqualTo(1L);
            assertThat(responseDto.getName()).isEqualTo(categoryName);
        }

        @Test
        @DisplayName("이미 있는 카테고리 생성할 경우")
        void createCategoryTest_Fail() {
            //given
            String categoryName = "category";
            NewCategoryRequestDto dto = new NewCategoryRequestDto(categoryName);
            given(categoryRepository.existsByUserAndName(user, dto.getName())).willReturn(true);

            //when then
            assertThatThrownBy(() -> bookmarkService.createCategory(user, dto))
                    .isInstanceOf(CreateCategoryFailedException.class)
                    .hasMessage("카테고리 생성에 실패했습니다: " + ALREADY_EXISTS_CATEGORY.errorMsg());
        }

        @Test
        @DisplayName("카테고리 삭제")
        void deleteCategoryTest() {
            //given
            String categoryName = "category";
            DeleteCategoryRequestDto dto = new DeleteCategoryRequestDto(categoryName);
            Category category = Category.builder()
                    .id(1L)
                    .user(user)
                    .name(categoryName)
                    .build();
            given(categoryRepository.existsByUserAndName(user, dto.getName())).willReturn(true);
            given(categoryRepository.findCategoryByUserAndName(user, dto.getName())).willReturn(Optional.of(category));

            //when
            CategoryResponseDto responseDto = bookmarkService.deleteCategory(user, dto);

            //then
            assertThat(responseDto.getId()).isEqualTo(1L);
            assertThat(responseDto.getName()).isEqualTo(categoryName);
        }

        @Test
        @DisplayName("없는 카테고리 삭제할 경우")
        void deleteCategoryTest_Fail() {
            //given
            String categoryName = "category";
            DeleteCategoryRequestDto dto = new DeleteCategoryRequestDto(categoryName);
            given(categoryRepository.existsByUserAndName(user, dto.getName())).willReturn(false);

            //when then
            assertThatThrownBy(() -> bookmarkService.deleteCategory(user, dto))
                    .isInstanceOf(CategoryNotFoundException.class);
        }
    }

}