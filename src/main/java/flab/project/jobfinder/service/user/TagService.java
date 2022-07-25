package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.TagDto;
import flab.project.jobfinder.dto.bookmark.TaggingRequestDto;
import flab.project.jobfinder.dto.bookmark.UnTagRequestDto;
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

import static flab.project.jobfinder.enums.bookmark.TagResponseCode.*;
import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.BOOKMARK_ID_NOT_FOUND;
import static flab.project.jobfinder.enums.exception.TagErrorCode.ALREADY_EXISTS_TAG;
import static flab.project.jobfinder.enums.exception.TagErrorCode.TAG_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final RecruitTagRepository recruitTagRepository;

    private final RecruitService recruitService;

    @Transactional
    public TagDto tag(User user, Long bookmarkId, TaggingRequestDto dto) {
        Recruit bookmark = recruitService.findById(user, bookmarkId)
                .orElseThrow(() -> new TagException(FAILED_TAGGING, BOOKMARK_ID_NOT_FOUND, bookmarkId));

        String tagName = dto.getTagName();
        if (recruitTagRepository.existsByRecruit_IdAndTag_Name(bookmarkId, tagName)) {
            throw new TagException(FAILED_TAGGING, ALREADY_EXISTS_TAG);
        }

        RecruitTag recruitTag = makeRecruitTag(bookmark, tagName);
        RecruitTag saveRecruitTag = recruitTagRepository.save(recruitTag);
        return new TagDto(saveRecruitTag.getTag());
    }

    @Transactional
    public TagDto untag(User user, UnTagRequestDto dto, Long bookmarkId) {
        Long tagId = Long.valueOf(dto.getTagId());
        RecruitTag recruitTag = recruitTagRepository.findByRecruit_IdAndTag_Id(bookmarkId, tagId)
                .orElseThrow(() -> new TagException(FAILED_UNTAGGING, TAG_NOT_FOUND, tagId));
        User bookmarkUser = recruitTag.getRecruit().getUser();
        if (!user.equals(bookmarkUser)) {
            throw new TagException(FAILED_UNTAGGING, BOOKMARK_ID_NOT_FOUND, tagId);
        }

        recruitTagRepository.delete(recruitTag);
        return new TagDto(recruitTag.getTag());
    }

    public int countByTag(TagDto tagDto) {
        return recruitTagRepository.countByTag_Id(tagDto.getId());
    }

    @Transactional
    public long remove(Long tagId) {
        return tagRepository.removeById(tagId);
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