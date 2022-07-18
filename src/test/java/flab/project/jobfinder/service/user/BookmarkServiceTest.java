package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.BookmarkNotFoundException;
import flab.project.jobfinder.exception.bookmark.CategoryNotFoundException;
import flab.project.jobfinder.exception.bookmark.CreateBookmarkFailedException;
import flab.project.jobfinder.exception.bookmark.CreateCategoryFailedException;
import flab.project.jobfinder.repository.CategoryRepository;
import flab.project.jobfinder.repository.RecruitRepository;
import org.junit.jupiter.api.*;
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
import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.REQUIRED_AT_LEAST_ONE_CATEGORY;
import static flab.project.jobfinder.enums.exception.CategoryErrorCode.ALREADY_EXISTS_CATEGORY;
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

        Category category1;
        Category category2;
        Category category3;
        List<Category> categoryList;

        @BeforeEach
        void init() {
            category1 = Category.builder()
                    .id(1L)
                    .user(user)
                    .name("category1").build();
            category2 = Category.builder()
                    .id(2L)
                    .user(user)
                    .name("category2").build();
            category3 = Category.builder()
                    .id(3L)
                    .user(user)
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
            given(categoryRepository.findAllByUser(user))
                    .willReturn(categoryList);

            //when
            List<CategoryResponseDto> categoryDto = bookmarkService.findCategoriesByUser(user);

            //then
            assertThat(categoryDto.size()).isEqualTo(3);
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
            given(categoryRepository.findAllByUser(user))
                    .willReturn(List.of(category));

            //when
            List<CategoryResponseDto> categoryDtoList = bookmarkService.createCategory(user, dto);

            //then
            assertThat(categoryDtoList.get(0).getId()).isEqualTo(1L);
            assertThat(categoryDtoList.get(0).getName()).isEqualTo(categoryName);
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
            Long categoryId = 1L;
            given(categoryRepository.existsByUserAndId(user, categoryId))
                    .willReturn(true);
            given(categoryRepository.findAllByUser(user))
                    .willReturn(List.of(category2, category3));

            //when
            List<CategoryResponseDto> responseDto = bookmarkService.deleteCategory(user, categoryId);

            //then
            int id = 2;
            for (CategoryResponseDto categoryResponseDto : responseDto) {
                assertThat(categoryResponseDto.getId()).isEqualTo(id);
                assertThat(categoryResponseDto.getName()).isEqualTo("category" + id);
                id++;
            }
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
        Category category1;
        Category category2;
        String categoryName1;
        String categoryName2;
        Recruit recruit;

        @BeforeEach
        void init() {
            categoryName1 = "category1";
            categoryName2 = "category2";
            recruitDto = RecruitDto.builder()
                    .career("career")
                    .corp("corp")
                    .dueDate(LocalDate.now().plusDays(1))
                    .isAlwaysRecruiting(false)
                    .jobType(FULL_TIME.name())
                    .location(GANGNAM.district())
                    .pay(ANNUAL.name())
                    .platform(JOBKOREA.koreaName())
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
            category1 = new Category(1L, categoryName1, user, List.of(recruit));
            category2 = new Category(2L, categoryName2, user, List.of(recruit));
        }

        @Test
        @DisplayName("북마크 생성 - 카테고리 한 개")
        void bookmarkedRecruitTest_oneCategory() {
            //given
            NewBookmarkRequestDto dto = new NewBookmarkRequestDto(List.of(categoryName1), recruitDto);
            List<BookmarkResponseDto> expect = List.of(new BookmarkResponseDto(1L, categoryName1, recruitDto));
            given(categoryRepository.findByUserAndName(user, categoryName1))
                    .willReturn(Optional.of(category1));
            given(recruitRepository.save(any()))
                    .willReturn(recruit);

            //when
            List<BookmarkResponseDto> result = bookmarkService.bookmarkRecruit(user, dto);

            //then
            assertThat(result.get(0).getId()).isEqualTo(expect.get(0).getId());
            assertThat(result.get(0).getCategoryName()).isEqualTo(expect.get(0).getCategoryName());
        }

        @Test
        @DisplayName("북마크 생성 - 카테고리 여러개에 북마크 할 경우")
        void bookmarkedRecruitTest_moreThanOneCategory() {
            //given
            NewBookmarkRequestDto dto = new NewBookmarkRequestDto(List.of("category1", "category2"), recruitDto);
            given(categoryRepository.findByUserAndName(user, "category1")).willReturn(Optional.of(category1));
            given(categoryRepository.findByUserAndName(user, "category2")).willReturn(Optional.of(category2));
            given(recruitRepository.save(any()))
                    .willReturn(recruit);

            //when
            List<BookmarkResponseDto> result = bookmarkService.bookmarkRecruit(user, dto);

            //then
            int num = 1;
            for (BookmarkResponseDto responseDto : result) {
                assertThat(responseDto.getId()).isEqualTo(1);
                assertThat(responseDto.getCategoryName()).isEqualTo("category" + num++);
            }

        }

        @Test
        @DisplayName("북마크 생성 실패- 카테고리 선택 안 한 경우")
        void bookmarkedRecruitTest_Fail_noCategoryInList() {
            //given
            NewBookmarkRequestDto dto = new NewBookmarkRequestDto(List.of(), recruitDto);

            //when then
            assertThatThrownBy(() -> bookmarkService.bookmarkRecruit(user, dto))
                    .isInstanceOf(CreateBookmarkFailedException.class)
                    .hasMessage(new CreateBookmarkFailedException(dto, REQUIRED_AT_LEAST_ONE_CATEGORY).getMessage());
        }

        @Test
        @DisplayName("북마크 생성 실패- 없는 카테고리 선택한 경우")
        void bookmarkedRecruitTest_Fail_noCategoryName() {
            //given
            NewBookmarkRequestDto dto = new NewBookmarkRequestDto(List.of("없는 카테고리 이름"), recruitDto);

            //when then
            assertThatThrownBy(() -> bookmarkService.bookmarkRecruit(user, dto))
                    .isInstanceOf(CategoryNotFoundException.class)
                    .hasMessage(new CategoryNotFoundException("없는 카테고리 이름").getMessage());
        }

        @Test
        @Disabled
        @DisplayName("북마크 삭제")
        void unbookmarkedRecruitTest() {
            //given
            Long categoryId = 1L;
            Long bookmarkId = 1L;
            Category category = new Category(1L, "category", user, List.of());
            given(categoryRepository.existsByUserAndId(user, categoryId))
                    .willReturn(true);
            given(recruitRepository.findById(bookmarkId))
                    .willReturn(Optional.of(recruit));

            //when
            List<BookmarkResponseDto> result = bookmarkService.unbookmarkRecruit(user, categoryId, bookmarkId);

            //then
            assertThat(result.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("유저에게 없는 북마크 삭제하는 경우")
        void unbookmarkedRecruitTest_NotExits_Fail() {
            //given
            Long categoryId = 1L;
            Long bookmarkId = 2L;
            given(categoryRepository.existsByUserAndId(user, categoryId))
                    .willReturn(true);
            given(recruitRepository.findById(bookmarkId))
                    .willThrow(new BookmarkNotFoundException(bookmarkId));

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
                    .name(categoryName1)
                    .recruits(List.of(recruit, recruit2, recruit3))
                    .build();
            given(categoryRepository.findByUserAndId(user, categoryId))
                    .willReturn(Optional.of(category));

            //when
            List<BookmarkResponseDto> result = bookmarkService.findAllBookmarksByCategory(user, categoryId);

            Long id = 1L;
            for (BookmarkResponseDto responseDto : result) {
                assertThat(responseDto.getId()).isEqualTo(id);
                assertThat(responseDto.getCategoryName()).isEqualTo(categoryName1);
                id++;
            }
        }
    }
}