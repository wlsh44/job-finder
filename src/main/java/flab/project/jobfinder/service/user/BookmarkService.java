package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.BookmarkException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static flab.project.jobfinder.enums.bookmark.BookmarkResponseCode.FAILED_CREATE_BOOKMARK;
import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.REQUIRED_AT_LEAST_ONE_CATEGORY;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final CategoryService categoryService;
    private final RecruitService recruitService;
    private final TagService tagService;

    public List<CategoryResponseDto> createCategory(User user, NewCategoryRequestDto dto) {
        categoryService.create(user, dto);
        return categoryService.findAllByUser(user);
    }

    public List<CategoryResponseDto> findAllCategoryByUser(User user) {
        return categoryService.findAllByUser(user);
    }

    public List<CategoryResponseDto> deleteCategory(User user, Long categoryId) {
        //TODO
        categoryService.delete(user, categoryId);
        return categoryService.findAllByUser(user);
    }

    public List<BookmarkResponseDto> findAllBookmarkByCategory(User user, Long categoryId) {
        return recruitService.findAllByCategory(user, categoryId);
    }

    public List<BookmarkResponseDto> bookmark(User user, NewBookmarkRequestDto dto) {
        if (dto.getCategoryList().isEmpty()) {
            throw new BookmarkException(FAILED_CREATE_BOOKMARK, REQUIRED_AT_LEAST_ONE_CATEGORY);
        }

        return recruitService.bookmark(user, dto);
    }

    public List<BookmarkResponseDto> unbookmark(User user, Long categoryId, Long bookmarkId) {
        BookmarkResponseDto unbookmark = recruitService.unbookmark(user, categoryId, bookmarkId);
        List<TagDto> tagList = unbookmark.getTagList();
        tagList.stream()
                .filter(tagDto -> tagService.countByTag(tagDto) == 0)
                .forEach(tagDto -> tagService.remove(tagDto.getId()));
        return recruitService.findAllByCategory(user, categoryId);
    }

    public TagDto tagging(User user, TaggingRequestDto dto, Long bookmarkId) {
        return tagService.tag(user, bookmarkId, dto);
    }

    public void untagging(User user, UnTagRequestDto dto, Long bookmarkId) {
        TagDto untag = tagService.untag(user, dto, bookmarkId);
        if (tagService.countByTag(untag) == 0) {
            tagService.remove(Long.valueOf(dto.getTagId()));
        }
    }
}
