package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.BookmarkNotFoundException;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static flab.project.jobfinder.enums.JobType.FULL_TIME;
import static flab.project.jobfinder.enums.Location.GANGNAM;
import static flab.project.jobfinder.enums.PayType.ANNUAL;
import static flab.project.jobfinder.enums.Platform.JOBKOREA;
import static flab.project.jobfinder.enums.Platform.ROCKETPUNCH;
import static flab.project.jobfinder.enums.exception.CreateCategoryFailedErrorCode.ALREADY_EXISTS_CATEGORY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    User user;

    @BeforeEach
    void init() {
        user = User.builder()
                .email("test@test.test")
                .name("test")
                .password("test")
                .build();
    }

    @Nested
    @DisplayName("카테고리 테스트")
    class CategoryTest {


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
            given(categoryRepository.findAllByUser(user))
                    .willReturn(categoryList);

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
            given(categoryRepository.existsByUserAndName(user, dto.getName()))
                    .willReturn(false);
            given(categoryRepository.save(any()))
                    .willReturn(category);

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
            given(categoryRepository.existsByUserAndName(user, dto.getName()))
                    .willReturn(true);

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
            Long categoryId = 1L;
            Category category = Category.builder()
                    .id(categoryId)
                    .user(user)
                    .name(categoryName)
                    .build();
            given(categoryRepository.existsByUserAndId(user, categoryId))
                    .willReturn(true);
            given(categoryRepository.findByUserAndId(user, categoryId))
                    .willReturn(Optional.of(category));

            //when
            CategoryResponseDto responseDto = bookmarkService.deleteCategory(user, categoryId);

            //then
            assertThat(responseDto.getId()).isEqualTo(1L);
            assertThat(responseDto.getName()).isEqualTo(categoryName);
        }

        @Test
        @DisplayName("없는 카테고리 삭제할 경우")
        void deleteCategoryTest_Fail() {
            //given
            Long categoryId = 1L;
            given(categoryRepository.existsByUserAndId(user, categoryId))
                    .willReturn(false);

            //when then
            assertThatThrownBy(() -> bookmarkService.deleteCategory(user, categoryId))
                    .isInstanceOf(CategoryNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("북마크 테스트")
    class BookmarkTest {

        RecruitDto recruitDto;
        Category category;
        String categoryName;
        Recruit recruit;

        @BeforeEach
        void init() {
            categoryName = "category";
            recruitDto = RecruitDto.builder()
                    .career("career")
                    .corp("corp")
                    .dueDate(LocalDate.now().plusDays(1))
                    .isAlwaysRecruiting(false)
                    .jobType(FULL_TIME.name())
                    .location(GANGNAM.district())
                    .pay(ANNUAL.name())
                    .platform(JOBKOREA.name())
                    .techStack("tech")
                    .title("title")
                    .url("url")
                    .build();
            recruit = Recruit.builder()
                    .career("career")
                    .corp("corp")
                    .dueDate(LocalDate.now().plusDays(1))
                    .isAlwaysRecruiting(false)
                    .jobType(FULL_TIME.name())
                    .location(GANGNAM.district())
                    .platform(JOBKOREA)
                    .techStack("tech")
                    .title("title")
                    .url("url")
                    .id(1L)
                    .build();
            category = Category.builder()
                    .id(1L)
                    .user(user)
                    .name(categoryName)
                    .recruits(List.of(recruit))
                    .build();
        }

        @Test
        @DisplayName("북마크 생성")
        void bookmarkedRecruitTest() {
            //given
            Long categoryId = 1L;
            NewBookmarkRequestDto dto = new NewBookmarkRequestDto(categoryName, recruitDto);
            BookmarkResponseDto expect = new BookmarkResponseDto(1L, recruitDto);
            given(categoryRepository.findByUserAndId(user, categoryId))
                    .willReturn(Optional.of(category));
            given(recruitRepository.save(any()))
                    .willReturn(recruit);

            //when
            BookmarkResponseDto result = bookmarkService.bookmarkRecruit(user, categoryId, dto);

            //then
            assertThat(result.getId()).isEqualTo(expect.getId());
        }

        @Test
        @DisplayName("북마크 삭제")
        void unbookmarkedRecruitTest() {
            //given
            Long categoryId = 1L;
            Long bookmarkId = 1L;
            BookmarkResponseDto expect = new BookmarkResponseDto(1L, recruitDto);
            given(categoryRepository.findByUserAndId(user, categoryId))
                    .willReturn(Optional.of(category));

            //when
            BookmarkResponseDto result = bookmarkService.unbookmarkRecruit(user, categoryId, bookmarkId);

            //then
            assertThat(result.getId()).isEqualTo(expect.getId());
        }

        @Test
        @DisplayName("유저에게 없는 북마크 삭제하는 경우")
        void unbookmarkedRecruitTest_NotExits_Fail() {
            //given
            Long categoryId = 1L;
            Long bookmarkId = 2L;
            given(categoryRepository.findByUserAndId(user, categoryId))
                    .willReturn(Optional.of(category));

            //when then
            assertThatThrownBy(() -> bookmarkService.unbookmarkRecruit(user, categoryId, bookmarkId))
                    .isInstanceOf(BookmarkNotFoundException.class);
        }

        @Test
        @DisplayName("북마크 전체 조회")
        void findAllBookmarksByCategory() {
            //given
            Long categoryId = 1L;
            Recruit recruit2 = Recruit.builder().id(2L)
                    .platform(JOBKOREA)
                    .build();
            Recruit recruit3 = Recruit.builder().id(3L)
                    .platform(ROCKETPUNCH)
                    .build();
            Category category = Category.builder()
                    .name(categoryName)
                    .recruits(List.of(recruit, recruit2, recruit3))
                    .build();
            given(categoryRepository.findByUserAndId(user, categoryId))
                    .willReturn(Optional.of(category));

            //when
            List<BookmarkResponseDto> result = bookmarkService.findAllBookmarksByCategory(user, categoryId);

            Long id = 1L;
            for (BookmarkResponseDto responseDto : result) {
                assertThat(responseDto.getId()).isEqualTo(id);
                id++;
            }
        }
    }
}