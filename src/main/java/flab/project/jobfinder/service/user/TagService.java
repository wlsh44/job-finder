package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.NewTagRequestDto;
import flab.project.jobfinder.dto.bookmark.TagDto;
import flab.project.jobfinder.dto.bookmark.TaggingRequestDto;
import flab.project.jobfinder.dto.bookmark.UnTagRequestDto;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.recruit.RecruitTag;
import flab.project.jobfinder.entity.recruit.Tag;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.CreateTagFailedException;
import flab.project.jobfinder.exception.bookmark.RemoveTagFailedException;
import flab.project.jobfinder.exception.bookmark.TaggingFailedException;
import flab.project.jobfinder.exception.bookmark.UnTaggingFailedException;
import flab.project.jobfinder.repository.RecruitRepository;
import flab.project.jobfinder.repository.RecruitTagRepository;
import flab.project.jobfinder.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.BOOKMARK_ID_NOT_FOUND;
import static flab.project.jobfinder.enums.exception.TagErrorCode.ALREADY_EXISTS_TAG;
import static flab.project.jobfinder.enums.exception.TagErrorCode.TAG_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TagService {

    private final RecruitRepository recruitRepository;
    private final TagRepository tagRepository;
    private final RecruitTagRepository recruitTagRepository;

    @Transactional
    public List<TagDto> createTag(User user, NewTagRequestDto dto) {
        if (tagRepository.existsByNameAndUser(dto.getName(), user)) {
            throw new CreateTagFailedException(dto, ALREADY_EXISTS_TAG);
        }

        tagRepository.save(dto.toEntity(user));
        return findTagsByUser(user);
    }

    public List<TagDto> findTagsByUser(User user) {
        return tagRepository.findTagsByUser(user)
                .stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TagDto> tagging(User user, Long bookmarkId, TaggingRequestDto dto) {
        Recruit bookmark = recruitRepository.findById(bookmarkId).orElseThrow(
                () -> new TaggingFailedException(BOOKMARK_ID_NOT_FOUND, bookmarkId));
        List<String> newTagList = dto.getTagList();

        List<TagDto> tagDtoList = newTagList.stream()
                .filter(tagName -> !recruitTagRepository.existsByRecruit_IdAndTag_Name(bookmarkId, tagName))
                .map(tagName -> makeRecruitTag(user, bookmark, tagName))
                .map(recruitTagRepository::save)
                .map(recruitTag -> new TagDto(recruitTag.getTag()))
                .collect(Collectors.toList());
        return tagDtoList;
    }

    private RecruitTag makeRecruitTag(User user, Recruit bookmark, String tagName) {
        Tag tag = tagRepository.findByUserAndName(user, tagName)
                .orElseThrow(() -> new TaggingFailedException(TAG_NOT_FOUND, tagName));
        return RecruitTag.builder()
                .recruit(bookmark)
                .tag(tag)
                .build();
    }

    private List<Tag> getTagsByBookmark(Recruit bookmark) {
        return bookmark.getRecruitTagList()
                .stream()
                .map(RecruitTag::getTag)
                .toList();
    }

    @Transactional
    public void untagging(User user, UnTagRequestDto dto, Long bookmarkId) {
        Long tagId = Long.valueOf(dto.getTagId());
        if (!recruitTagRepository.existsByRecruit_IdAndTag_Id(bookmarkId, tagId)) {
            throw new UnTaggingFailedException(TAG_NOT_FOUND, tagId);
        }
        recruitTagRepository.deleteByRecruit_idAndTag_Id(bookmarkId, tagId);
    }

    @Transactional
    public void removeTag(User user, Long tagId) {
        Tag tag = getTagById(tagId, () -> new RemoveTagFailedException(TAG_NOT_FOUND, tagId));
        recruitTagRepository.deleteAllInBatch(tag.getRecruitTagList());
        if (!tag.getUser().equals(user)) {
            throw new RemoveTagFailedException(TAG_NOT_FOUND, tagId);
        }
        tagRepository.delete(tag);
    }

    private Tag getTagById(Long tagId, Supplier<? extends RuntimeException> e) {
        return tagRepository.findById(tagId)
                .orElseThrow(e);
    }

    public List<TagDto> findTagByUser(User user) {
        return tagRepository.findTagsByUser(user)
                .stream()
                .map(TagDto::new).toList();
    }

    public List<TagDto> getTagsDtoByBookmark(Recruit bookmark) {
        List<Tag> tagList = getTagsByBookmark(bookmark);
        return tagList.stream()
                .map(TagDto::new)
                .toList();
    }
}
