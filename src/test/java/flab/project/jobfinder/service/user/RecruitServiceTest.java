package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.dto.page.PageDto;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.repository.RecruitRepository;
import flab.project.jobfinder.service.user.pagination.BookmarkPagination;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RecruitServiceTest {

    @InjectMocks
    RecruitService recruitService;

    @Mock
    RecruitRepository recruitRepository;

    @Mock
    BookmarkPagination bookmarkPagination;

    User user;
    RecruitDto recruitDto;
    Category category1;
    Category category2;
    String categoryName1;
    String categoryName2;
    Recruit recruit;

    @BeforeEach
    void init() {
        user = User.builder()
                .email("test@test.test")
                .name("test")
                .password("test")
                .build();

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
                .id(1L)
                .category(category1)
                .recruitTagList(new ArrayList<>())
                .build();
        category1.getRecruits().add(recruit);
    }

    @Test
    @DisplayName("북마크 생성")
    void bookmarkedRecruitTest_oneCategory() {
        //given
        BookmarkResponseDto expect = new BookmarkResponseDto(1L, new RecruitDto(recruit), categoryName1, null);
        given(recruitRepository.save(any()))
                .willReturn(recruit);

        //when
        BookmarkResponseDto result = recruitService.bookmark(user, recruitDto, category1);

        //then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("북마크 삭제")
    void unbookmarkedRecruitTest() {
        //given

        //when
        long unBookmarkId = recruitService.unbookmark(recruit);

        //then
        assertThat(unBookmarkId).isEqualTo(1);
    }

    @Test
    @DisplayName("북마크 전체 조회")
    void findAllBookmarksByCategory() {
        //given
        Long categoryId = 1L;
        Recruit recruit2 = Recruit.builder()
                .id(2L)
                .platform(JOBKOREA)
                .category(category1)
                .recruitTagList(new ArrayList<>())
                .build();
        category1.getRecruits().add(recruit2);
        Pageable pageable = PageRequest.of(0, 20);
        Page<Recruit> page = new PageImpl<>(List.of(recruit, recruit2), pageable, 3);
        PageDto pageDto = PageDto.builder()
                .totalPage(1)
                .startPage(1)
                .build();
        BookmarkPageDto expect = new BookmarkPageDto(List.of(
                new BookmarkResponseDto(recruit.getId(), new RecruitDto(recruit), recruit.getCategory().getName(), new ArrayList<>()),
                new BookmarkResponseDto(recruit2.getId(), new RecruitDto(recruit2), recruit2.getCategory().getName(), new ArrayList<>())),
                pageDto
        );
        given(recruitRepository.findRecruits(user, categoryId, pageable))
                .willReturn(page);
        given(bookmarkPagination.toPageDto(page))
                .willReturn(pageDto);

        //when
        BookmarkPageDto result = recruitService.findByCategory(user, categoryId, pageable);

        //then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("북마크 아이디로 조회")
    void findByIdTest() {
        //given
        Long bookmarkId = 1L;
        given(recruitRepository.findRecruit(user, bookmarkId))
                .willReturn(recruit);

        //when
        Optional<Recruit> res = recruitService.findById(user, bookmarkId);

        //then
        assertThat(res).isEqualTo(Optional.of(recruit));
    }

    @Test
    @DisplayName("북마크 아이디로 조회 - 없을 경우")
    void findByIdTest_Fail() {
        //given
        Long bookmarkId = 1L;
        given(recruitRepository.findRecruit(user, bookmarkId))
                .willReturn(null);

        //when
        Optional<Recruit> res = recruitService.findById(user, bookmarkId);

        //then
        assertThat(res).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("카테고리 아이디, 북마크 아이디로 조회")
    void findByCategoryIdAndBookmarkIdTest() {
        //given
        Long bookmarkId = 1L;
        Long categoryId = 1L;
        given(recruitRepository.findRecruit(user, categoryId, bookmarkId))
                .willReturn(recruit);

        //when
        Optional<Recruit> res = recruitService.findByCategoryIdAndBookmarkId(user, categoryId, bookmarkId);

        //then
        assertThat(res).isEqualTo(Optional.of(recruit));
    }

    @Test
    @DisplayName("카테고리 아이디, 북마크 아이디로 조회 - 없을 경우")
    void findByCategoryIdAndBookmarkIdTest_Fail() {
        //given
        Long bookmarkId = 1L;
        Long categoryId = 1L;
        given(recruitRepository.findRecruit(user, categoryId, bookmarkId))
                .willReturn(null);

        //when
        Optional<Recruit> res = recruitService.findByCategoryIdAndBookmarkId(user, categoryId, bookmarkId);

        //then
        assertThat(res).isEqualTo(Optional.empty());
    }
}