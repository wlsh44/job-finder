package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.TagResponseDto;
import flab.project.jobfinder.dto.bookmark.TaggingRequestDto;
import flab.project.jobfinder.dto.bookmark.UnTaggingRequestDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.recruit.RecruitTag;
import flab.project.jobfinder.entity.recruit.Tag;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.TagException;
import flab.project.jobfinder.repository.RecruitTagRepository;
import flab.project.jobfinder.repository.TagRepository;
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

import static flab.project.jobfinder.enums.bookmark.TagResponseCode.FAILED_TAGGING;
import static flab.project.jobfinder.enums.bookmark.TagResponseCode.FAILED_UNTAGGING;
import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.BOOKMARK_ID_NOT_FOUND;
import static flab.project.jobfinder.enums.exception.TagErrorCode.ALREADY_EXISTS_TAG;
import static flab.project.jobfinder.enums.exception.TagErrorCode.TAG_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @InjectMocks
    TagService tagService;

    @Mock
    TagRepository tagRepository;

    @Mock
    RecruitTagRepository recruitTagRepository;

    String tagName;
    String categoryName;

    User user;
    Category category;
    Recruit recruit;
    Tag tag;
    RecruitTag recruitTag;

    @BeforeEach
    void init() {
        user = User.builder()
                .email("test@test.test")
                .name("test")
                .password("test")
                .build();
        categoryName = "category";
        tagName = "tag1";
        category = new Category(1L, categoryName, user, new ArrayList<>());
        recruit = Recruit.builder()
                .id(1L)
                .category(category)
                .recruitTagList(new ArrayList<>())
                .user(user)
                .build();
        tag = Tag.builder()
                .id(1L)
                .name(tagName)
                .build();
        recruitTag = RecruitTag.builder()
                .tag(tag)
                .recruit(recruit)
                .id(1L)
                .build();
        recruit.getRecruitTagList().addAll(List.of(recruitTag));
        category.getRecruits().add(recruit);
    }

    @Test
    @DisplayName("없는 태그를 태깅 하는 경우")
    void tagTest_NotExist() {
        //given
        String tagName = "newTagName";
        TaggingRequestDto dto = new TaggingRequestDto();
        dto.setTagName(tagName);
        Tag newTag = Tag.builder()
                .id(2L)
                .name(tagName)
                .build();
        RecruitTag recruitTag = RecruitTag.builder()
                .recruit(recruit)
                .tag(newTag)
                .build();
        TagResponseDto expect = new TagResponseDto(recruitTag.getTag());
        given(recruitTagRepository.existsByRecruit_IdAndTag_Name(recruit.getId(), tagName))
                .willReturn(false);
        given(tagRepository.findByName(tagName))
                .willReturn(Optional.empty());
        given(recruitTagRepository.save(recruitTag))
                .willReturn(recruitTag);

        //when
        TagResponseDto res = tagService.tag(recruit, dto);

        //then
        assertThat(res)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("있는 태그를 태깅 하는 경우")
    void tagTest_Exist() {
        //given
        TaggingRequestDto dto = new TaggingRequestDto();
        dto.setTagName(tagName);
        TagResponseDto expect = new TagResponseDto(recruitTag.getTag());
        given(recruitTagRepository.existsByRecruit_IdAndTag_Name(recruit.getId(), tagName))
                .willReturn(false);
        given(tagRepository.findByName(tagName))
                .willReturn(Optional.of(tag));
        given(recruitTagRepository.save(any()))
                .willReturn(recruitTag);

        //when
        TagResponseDto res = tagService.tag(recruit, dto);

        //then
        assertThat(res)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("북마크에 해당 태그가 존재하는 경우")
    void tagTest_Fail() {
        //given
        TaggingRequestDto dto = new TaggingRequestDto();
        dto.setTagName(tagName);
        TagResponseDto expect = new TagResponseDto(recruitTag.getTag());
        given(recruitTagRepository.existsByRecruit_IdAndTag_Name(recruit.getId(), tagName))
                .willReturn(true);

        //when then
        assertThatThrownBy(() -> tagService.tag(recruit, dto))
                .isInstanceOf(TagException.class)
                .hasMessage(new TagException(FAILED_TAGGING, ALREADY_EXISTS_TAG).getMessage());
    }

    @Test
    @DisplayName("태그 삭제")
    void untag() {
        //given
        Long tagId = 1L;
        Long bookmarkId = 1L;
        UnTaggingRequestDto dto = new UnTaggingRequestDto();
        dto.setTagId(tagId.toString());
        given(recruitTagRepository.findRecruitTag(bookmarkId, tagId))
                .willReturn(Optional.of(recruitTag));

        //when
        long res = tagService.untag(user, dto, bookmarkId);

        //then
        assertThat(res).isEqualTo(recruitTag.getId());
    }

    @Test
    @DisplayName("해당 유저의 태그가 아닌 태그를 삭제할 경우")
    void untag_NotExistTag_Fail() {
        //given
        Long tagId = 1L;
        Long bookmarkId = 1L;
        UnTaggingRequestDto dto = new UnTaggingRequestDto();
        dto.setTagId(tagId.toString());
        given(recruitTagRepository.findRecruitTag(bookmarkId, tagId))
                .willReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> tagService.untag(user, dto, bookmarkId))
                .isInstanceOf(TagException.class)
                .hasMessage(new TagException(FAILED_UNTAGGING, TAG_NOT_FOUND, tagId).getMessage());
    }


    @Test
    @DisplayName("해당 유저의 태그가 아닌 태그를 삭제할 경우")
    void untag_NotUserTag_Fail() {
        //given
        User newUser = User.builder()
                .id(2L)
                .build();
        Long tagId = 1L;
        Long bookmarkId = 1L;
        UnTaggingRequestDto dto = new UnTaggingRequestDto();
        dto.setTagId(tagId.toString());
        given(recruitTagRepository.findRecruitTag(bookmarkId, tagId))
                .willReturn(Optional.of(recruitTag));

        //when then
        assertThatThrownBy(() -> tagService.untag(newUser, dto, bookmarkId))
                .isInstanceOf(TagException.class)
                .hasMessage(new TagException(FAILED_UNTAGGING, BOOKMARK_ID_NOT_FOUND, tagId).getMessage());
    }


    @Test
    void deleteAllRecruitTag() {
    }

    @Test
    void removeIfTaggedOnlyOneBookmark() {
    }
}