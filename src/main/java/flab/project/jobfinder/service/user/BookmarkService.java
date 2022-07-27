package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.BookmarkException;
import flab.project.jobfinder.exception.bookmark.CategoryException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static flab.project.jobfinder.enums.bookmark.BookmarkResponseCode.*;
import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.BOOKMARK_ID_NOT_FOUND;
import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.REQUIRED_AT_LEAST_ONE_CATEGORY;
import static flab.project.jobfinder.enums.exception.CategoryErrorCode.CATEGORY_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final CategoryService categoryService;
    private final RecruitService recruitService;
    private final TagService tagService;

    @Transactional
    public List<CategoryResponseDto> createCategory(User user, NewCategoryRequestDto dto) {
        categoryService.create(user, dto);
        return categoryService.findAllByUser(user);
    }

    public List<CategoryResponseDto> findAllCategoryByUser(User user) {
        return categoryService.findAllByUser(user);
    }

    @Transactional
    public List<CategoryResponseDto> deleteCategory(User user, Long categoryId) {
        Category category = categoryService.findByUserAndId(user, categoryId)
                .orElseThrow(() -> new CategoryException(FAILED_DELETE_BOOKMARK, CATEGORY_ID_NOT_FOUND, categoryId));
        category.getRecruits()
                .forEach(tagService::deleteAllRecruitTag);
        categoryService.delete(categoryId);
        return categoryService.findAllByUser(user);
    }

    public BookmarkPageDto findBookmarkByCategory(User user, Long categoryId, Pageable pageable) {
        return recruitService.findByCategory(user, categoryId, pageable);
    }

    @Transactional
    public List<BookmarkResponseDto> bookmark(User user, NewBookmarkRequestDto dto) {
        List<String> categoryNameList = dto.getCategoryList();
        if (categoryNameList.isEmpty()) {
            throw new BookmarkException(FAILED_CREATE_BOOKMARK, REQUIRED_AT_LEAST_ONE_CATEGORY);
        }

        List<Category> categoryList = categoryService.findByNameIn(dto.getCategoryList());
        return recruitService.bookmark(user, dto.getRecruitDto(), categoryList);
    }

    @Transactional
    public BookmarkPageDto unbookmark(User user, Long categoryId, Long bookmarkId, Pageable pageable) {
        Recruit bookmark = recruitService.findByCategoryIdAndBookmarkId(user, categoryId, bookmarkId)
                .orElseThrow(() -> new BookmarkException(FAILED_DELETE_BOOKMARK, BOOKMARK_ID_NOT_FOUND, bookmarkId));

        tagService.deleteAllRecruitTag(bookmark);
        recruitService.unbookmark(bookmark);
        return recruitService.findByCategory(user, categoryId, pageable);
    }

    @Transactional
    public TagResponseDto tagging(User user, TaggingRequestDto dto, Long bookmarkId) {
        return tagService.tag(user, bookmarkId, dto);
    }

    @Transactional
    public void untagging(User user, UnTaggingRequestDto dto, Long bookmarkId) {
        tagService.untag(user, dto, bookmarkId);
        tagService.removeIfTaggedOnlyOneBookmark(Long.valueOf(dto.getTagId()));
    }
}
