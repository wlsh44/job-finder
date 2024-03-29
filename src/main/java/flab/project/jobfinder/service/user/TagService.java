package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.TagResponseDto;
import flab.project.jobfinder.dto.bookmark.TaggingRequestDto;
import flab.project.jobfinder.dto.bookmark.UnTaggingRequestDto;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.recruit.RecruitTag;
import flab.project.jobfinder.entity.recruit.Tag;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.*;
import flab.project.jobfinder.repository.RecruitTagRepository;
import flab.project.jobfinder.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static flab.project.jobfinder.enums.bookmark.TagResponseCode.*;
import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.BOOKMARK_ID_NOT_FOUND;
import static flab.project.jobfinder.enums.exception.TagErrorCode.ALREADY_EXISTS_TAG;
import static flab.project.jobfinder.enums.exception.TagErrorCode.TAG_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final RecruitTagRepository recruitTagRepository;

    @Transactional
    public TagResponseDto tag(Recruit bookmark, TaggingRequestDto dto) {
        String tagName = dto.getTagName();
        if (recruitTagRepository.existsByRecruit_IdAndTag_Name(bookmark.getId(), tagName)) {
            throw new TagException(FAILED_TAGGING, ALREADY_EXISTS_TAG);
        }

        RecruitTag recruitTag = makeRecruitTag(bookmark, tagName);
        RecruitTag saveRecruitTag = recruitTagRepository.save(recruitTag);
        return new TagResponseDto(saveRecruitTag.getTag());
    }

    @Transactional
    public long untag(User user, UnTaggingRequestDto dto, Long bookmarkId) {
        Long tagId = Long.valueOf(dto.getTagId());
        RecruitTag recruitTag = recruitTagRepository.findRecruitTag(bookmarkId, tagId)
                .orElseThrow(() -> new TagException(FAILED_UNTAGGING, TAG_NOT_FOUND, tagId));

        User bookmarkUser = recruitTag.getRecruit().getUser();
        if (!user.equals(bookmarkUser)) {
            throw new TagException(FAILED_UNTAGGING, BOOKMARK_ID_NOT_FOUND, tagId);
        }

        recruitTagRepository.delete(recruitTag);
        return recruitTag.getId();
    }

    @Transactional
    public long deleteAllRecruitTag(Recruit bookmark) {
        List<RecruitTag> recruitTagList = bookmark.getRecruitTagList();
        recruitTagRepository.deleteAllInBatch(recruitTagList);
        recruitTagList.stream()
                .map(RecruitTag::getTag)
                .forEach(tag -> removeIfTaggedOnlyOneBookmark(tag.getId()));
        return 0;
    }

    @Transactional
    public long removeIfTaggedOnlyOneBookmark(Long tagId) {
        if (recruitTagRepository.countRecruitTag(tagId) == 0) {
            tagRepository.deleteById(tagId);
        }
        return tagId;
    }

    private RecruitTag makeRecruitTag(Recruit bookmark, String tagName) {
        Tag tag = tagRepository.findByName(tagName)
                .orElseGet(() -> saveTag(tagName));
        return RecruitTag.builder()
                .recruit(bookmark)
                .tag(tag)
                .build();
    }

    private Tag saveTag(String tagName) {
        return tagRepository.save(Tag.builder()
                .name(tagName)
                .build()
        );
    }
}
