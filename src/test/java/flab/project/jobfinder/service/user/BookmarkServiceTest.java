package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.dto.page.PageDto;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.recruit.RecruitTag;
import flab.project.jobfinder.entity.recruit.Tag;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static flab.project.jobfinder.enums.JobType.FULL_TIME;
import static flab.project.jobfinder.enums.Location.GANGNAM;
import static flab.project.jobfinder.enums.PayType.ANNUAL;
import static flab.project.jobfinder.enums.Platform.JOBKOREA;
import static flab.project.jobfinder.enums.Platform.ROCKETPUNCH;
import static flab.project.jobfinder.enums.bookmark.BookmarkResponseCode.FAILED_CREATE_BOOKMARK;
import static flab.project.jobfinder.enums.bookmark.BookmarkResponseCode.FAILED_DELETE_BOOKMARK;
import static flab.project.jobfinder.enums.bookmark.TagResponseCode.FAILED_TAGGING;
import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.*;
import static flab.project.jobfinder.enums.exception.CategoryErrorCode.*;
import static flab.project.jobfinder.enums.exception.TagErrorCode.ALREADY_EXISTS_TAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @InjectMocks
    BookmarkService bookmarkService;

    @Mock
    RecruitService recruitService;

    @Mock
    CategoryService categoryService;

    @Mock
    TagService tagService;

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
                    .name("category1")
                    .recruits(new ArrayList<>())
                    .build();
            category2 = Category.builder()
                    .id(2L)
                    .user(user)
                    .name("category2")
                    .recruits(new ArrayList<>())
                    .build();
            category3 = Category.builder()
                    .id(3L)
                    .user(user)
                    .name("category3")
                    .recruits(new ArrayList<>())
                    .build();
            categoryList = List.of(category1, category2, category3);
        }

        @Test
        @DisplayName("전체 카테고리 조회")
        void findCategoriesByUserTest() {
            //given
            List<CategoryResponseDto> categoryList = List.of(
                    new CategoryResponseDto(category1),
                    new CategoryResponseDto(category2),
                    new CategoryResponseDto(category3));
            given(categoryService.findAllByUser(user))
                    .willReturn(categoryList);

            //when
            List<CategoryResponseDto> categoryDto = bookmarkService.findAllCategoryByUser(user);

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
            List<CategoryResponseDto> categoryList = List.of(
                    new CategoryResponseDto(category));
            given(categoryService.findAllByUser(user))
                    .willReturn(categoryList);

            //when
            List<CategoryResponseDto> categoryDtoList = bookmarkService.createCategory(user, dto);

            //then
            assertThat(categoryDtoList.get(0).getId()).isEqualTo(1L);
            assertThat(categoryDtoList.get(0).getName()).isEqualTo(categoryName);
        }

        @Test
        @DisplayName("이미 있는 카테고리 생성할 경우")
        void createCategoryTest_AlreadyExistsCategory_Fail() {
            //given
            String categoryName = "category";
            NewCategoryRequestDto dto = new NewCategoryRequestDto(categoryName);
            given(categoryService.create(user, dto))
                    .willThrow(new CategoryException(FAILED_CREATE_BOOKMARK, ALREADY_EXISTS_CATEGORY));

            //when then
            assertThatThrownBy(() -> bookmarkService.createCategory(user, dto))
                    .isInstanceOf(CategoryException.class)
                    .hasMessage(new CategoryException(FAILED_CREATE_BOOKMARK, ALREADY_EXISTS_CATEGORY).getMessage());
        }

        @Test
        @DisplayName("카테고리 10개 초과 생성할 경우")
        void createCategoryTest_OverMaximumCategoryNum_Fail() {
            //given
            String categoryName = "category";
            NewCategoryRequestDto dto = new NewCategoryRequestDto(categoryName);
            given(categoryService.create(user, dto))
                    .willThrow(new CategoryException(FAILED_CREATE_BOOKMARK, TOO_MANY_CATEGORIES));

            //when then
            assertThatThrownBy(() -> bookmarkService.createCategory(user, dto))
                    .isInstanceOf(CategoryException.class)
                    .hasMessage(new CategoryException(FAILED_CREATE_BOOKMARK, TOO_MANY_CATEGORIES).getMessage());
        }

        @Test
        @DisplayName("카테고리 삭제")
        void deleteCategoryTest() {
            //given
            Long categoryId = 1L;
            List<CategoryResponseDto> categoryList = List.of(
                    new CategoryResponseDto(category2),
                    new CategoryResponseDto(category3));
            given(categoryService.findByUserAndId(user, categoryId))
                    .willReturn(Optional.of(category1));
            given(categoryService.findAllByUser(user))
                    .willReturn(categoryList);

            //when
            List<CategoryResponseDto> responseDto = bookmarkService.deleteCategory(user, categoryId);

            //then
            int id = 2;
            for (CategoryResponseDto categoryResponseDto : responseDto) {
                assertThat(categoryResponseDto.getId()).isEqualTo(id);
                assertThat(categoryResponseDto.getName()).isEqualTo("category" + id++);
            }
        }

        @Test
        @DisplayName("없는 카테고리 삭제할 경우")
        void deleteCategoryTest_Fail() {
            //given
            Long categoryId = 1L;
            given(categoryService.existsByUserAndId(user, categoryId))
                    .willReturn(false);

            //when then
            assertThatThrownBy(() -> bookmarkService.deleteCategory(user, categoryId))
                    .isInstanceOf(CategoryException.class)
                    .hasMessage(new CategoryException(FAILED_DELETE_BOOKMARK, CATEGORY_ID_NOT_FOUND).getMessage());
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
        Tag tag1;
        Tag tag2;
        RecruitTag recruitTag1;
        RecruitTag recruitTag2;

        @BeforeEach
        void init() {
            categoryName1 = "category1";
            categoryName2 = "category2";
            category1 = new Category(1L, categoryName1, user, new ArrayList<>());
            category2 = new Category(2L, categoryName2, user, new ArrayList<>());
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
                    .recruitTagList(new ArrayList<>())
                    .id(1L)
                    .build();
            tag1 = Tag.builder()
                    .id(1L)
                    .name("tag1")
                    .build();
            tag2 = Tag.builder()
                    .id(2L)
                    .name("tag2")
                    .build();
            recruitTag1 = RecruitTag.builder()
                    .tag(tag1)
                    .recruit(recruit)
                    .build();
            recruitTag2 = RecruitTag.builder()
                    .tag(tag2)
                    .recruit(recruit)
                    .build();
            recruit.getRecruitTagList().addAll(List.of(recruitTag1, recruitTag2));
            category1.getRecruits().add(recruit);
        }

        @Test
        @DisplayName("북마크 생성 - 카테고리 한 개")
        void bookmarkedRecruitTest_oneCategory() {
            //given
            List<String> categoryList = List.of(this.categoryName1);
            NewBookmarkRequestDto dto = new NewBookmarkRequestDto(categoryList, recruitDto);
            List<BookmarkResponseDto> expect = List.of(new BookmarkResponseDto(1L, recruitDto, this.categoryName1, null));
            given(categoryService.findByNameIn(categoryList))
                    .willReturn(List.of(category1));
            given(recruitService.bookmark(user, dto.getRecruitDto(), category1))
                    .willReturn(expect.get(0));

            //when
            List<BookmarkResponseDto> result = bookmarkService.bookmark(user, dto);

            //then
            assertThat(result.get(0).getId()).isEqualTo(expect.get(0).getId());
            assertThat(result.get(0).getCategoryName()).isEqualTo(expect.get(0).getCategoryName());
        }

        @Test
        @DisplayName("북마크 생성 - 카테고리 여러개에 북마크 할 경우")
        void bookmarkedRecruitTest_moreThanOneCategory() {
            //given
            List<String> categoryList = List.of(categoryName1, categoryName2);
            NewBookmarkRequestDto dto = new NewBookmarkRequestDto(categoryList, recruitDto);
            List<BookmarkResponseDto> expect = List.of(
                    new BookmarkResponseDto(1L, recruitDto, categoryName1, null),
                    new BookmarkResponseDto(2L, recruitDto, categoryName2, null)
            );
            given(categoryService.findByNameIn(categoryList))
                    .willReturn(List.of(category1, category2));
            given(recruitService.bookmark(user, dto.getRecruitDto(), category1))
                    .willReturn(expect.get(0));
            given(recruitService.bookmark(user, dto.getRecruitDto(), category2))
                    .willReturn(expect.get(1));

            //when
            List<BookmarkResponseDto> result = bookmarkService.bookmark(user, dto);

            //then
            int num = 1;
            for (BookmarkResponseDto responseDto : result) {
                assertThat(responseDto.getId()).isEqualTo(num);
                assertThat(responseDto.getCategoryName()).isEqualTo("category" + num++);
            }

        }

        @Test
        @DisplayName("북마크 생성 실패- 카테고리 선택 안 한 경우")
        void bookmarkedRecruitTest_Fail_noCategoryInList() {
            //given
            NewBookmarkRequestDto dto = new NewBookmarkRequestDto(List.of(), recruitDto);

            //when then
            assertThatThrownBy(() -> bookmarkService.bookmark(user, dto))
                    .isInstanceOf(BookmarkException.class)
                    .hasMessage(new BookmarkException(FAILED_CREATE_BOOKMARK, REQUIRED_AT_LEAST_ONE_CATEGORY).getMessage());
        }

        @Test
        @Disabled
        @DisplayName("북마크 삭제")
        void unbookmarkedRecruitTest() {
            //given
            Long categoryId = 1L;
            Long bookmarkId = 1L;
            Pageable pageable = PageRequest.of(1, 20);
            BookmarkPageDto bookmarkPageDto = new BookmarkPageDto(List.of(new BookmarkResponseDto(recruit.getId(), new RecruitDto(recruit),
                    recruit.getCategory().getName(), List.of(new TagResponseDto(tag1), new TagResponseDto(tag2)))), PageDto.builder().startPage(1).totalPage(1).build());
            given(recruitService.findByCategoryIdAndBookmarkId(user, categoryId, bookmarkId))
                    .willReturn(Optional.of(recruit));
            given(recruitService.findByCategory(user, categoryId, pageable))
                    .willReturn(bookmarkPageDto);

            //when
            BookmarkPageDto unbookmarkDto = bookmarkService.unbookmark(user, categoryId, bookmarkId, pageable);

            //then
            assertThat(unbookmarkDto).isEqualTo(bookmarkPageDto);
        }

        @Test
        @DisplayName("유저에게 없는 북마크 삭제하는 경우")
        void unbookmarkedRecruitTest_NotExits_Fail() {
            //given
            Long categoryId = 1L;
            Long bookmarkId = 2L;
            given(recruitService.findByCategoryIdAndBookmarkId(user, categoryId, bookmarkId))
                    .willReturn(Optional.empty());
            Pageable pageable = PageRequest.of(1, 20);

            //when then
            assertThatThrownBy(() -> bookmarkService.unbookmark(user, categoryId, bookmarkId, pageable))
                    .isInstanceOf(BookmarkException.class)
                    .hasMessage(new BookmarkException(FAILED_DELETE_BOOKMARK, BOOKMARK_ID_NOT_FOUND, bookmarkId).getMessage());
        }
    }

    @Nested
    @DisplayName("태그 테스트")
    class TagTest {
        RecruitDto recruitDto;
        Category category1;
        String categoryName1;
        Recruit recruit;
        Tag tag1;
        Tag tag2;
        RecruitTag recruitTag1;
        RecruitTag recruitTag2;

        @BeforeEach
        void init() {
            categoryName1 = "category1";
            category1 = new Category(1L, categoryName1, user, new ArrayList<>());
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
                    .recruitTagList(new ArrayList<>())
                    .id(1L)
                    .build();
            tag1 = Tag.builder()
                    .id(1L)
                    .name("tag1")
                    .build();
            tag2 = Tag.builder()
                    .id(2L)
                    .name("tag2")
                    .build();
            recruitTag1 = RecruitTag.builder()
                    .tag(tag1)
                    .recruit(recruit)
                    .build();
            recruitTag2 = RecruitTag.builder()
                    .tag(tag2)
                    .recruit(recruit)
                    .build();
            recruit.getRecruitTagList().addAll(List.of(recruitTag1, recruitTag2));
            category1.getRecruits().add(recruit);
        }

        @Test
        @DisplayName("태깅 성공")
        void taggingTest() {
            //given
            String tagName = "tag1";
            Long bookmarkId = 1L;
            TaggingRequestDto dto = new TaggingRequestDto();
            dto.setTagName(tagName);
            TagResponseDto expect = new TagResponseDto(recruitTag1.getTag());
            given(tagService.tag(recruit, dto))
                    .willReturn(expect);
            given(recruitService.findById(user, bookmarkId))
                    .willReturn(Optional.of(recruit));

            //when
            TagResponseDto tagging = bookmarkService.tagging(user, dto, bookmarkId);

            //then
            assertThat(tagging).isEqualTo(expect);
        }

        @Test
        @DisplayName("태깅 실패 - 북마크 없을 경우")
        void taggingTest_NoBookmark_Fail() {
            //given
            String tagName = "tag1";
            Long bookmarkId = 1L;
            TaggingRequestDto dto = new TaggingRequestDto();
            dto.setTagName(tagName);
            given(recruitService.findById(user, bookmarkId))
                    .willReturn(Optional.empty());

            //when then
            assertThatThrownBy(() -> bookmarkService.tagging(user, dto, bookmarkId))
                    .isInstanceOf(TagException.class)
                    .hasMessage(new TagException(FAILED_TAGGING, BOOKMARK_ID_NOT_FOUND, bookmarkId).getMessage());
        }

        @Test
        @DisplayName("태깅 실패 - 이미 존재하는 태그일 경우")
        void taggingTest_AlreadyTagged_Fail() {
            //given
            String tagName = "tag1";
            Long bookmarkId = 1L;
            TaggingRequestDto dto = new TaggingRequestDto();
            dto.setTagName(tagName);
            given(tagService.tag(recruit, dto))
                    .willThrow(new TagException(FAILED_TAGGING, ALREADY_EXISTS_TAG));
            given(recruitService.findById(user, bookmarkId))
                    .willReturn(Optional.of(recruit));

            //when then
            assertThatThrownBy(() -> bookmarkService.tagging(user, dto, bookmarkId))
                    .isInstanceOf(TagException.class)
                    .hasMessage(new TagException(FAILED_TAGGING, ALREADY_EXISTS_TAG).getMessage());
        }
    }

}