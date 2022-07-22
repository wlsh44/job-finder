package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.recruit.RecruitTag;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.*;
import flab.project.jobfinder.repository.RecruitRepository;
import flab.project.jobfinder.repository.RecruitTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static flab.project.jobfinder.enums.bookmark.BookmarkResponseCode.*;
import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.*;
import static flab.project.jobfinder.enums.exception.CategoryErrorCode.CATEGORY_ID_NOT_FOUND;
import static flab.project.jobfinder.enums.exception.CategoryErrorCode.CATEGORY_NAME_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final RecruitRepository recruitRepository;
    private final RecruitTagRepository recruitTagRepository;

    private final CategoryService categoryService;

    @Transactional
    public List<BookmarkResponseDto> bookmark(User user, NewBookmarkRequestDto dto) {
        List<String> categoryList = dto.getCategoryList();
        if (categoryList.isEmpty()) {
            throw new BookmarkException(FAILED_CREATE_BOOKMARK, REQUIRED_AT_LEAST_ONE_CATEGORY);
        }
        List<BookmarkResponseDto> responseDtoList = new ArrayList<>();

        for (String categoryName : categoryList) {
            Category category = categoryService.findByUserAndName(user, categoryName,
                    () -> new BookmarkException(FAILED_CREATE_BOOKMARK, CATEGORY_NAME_NOT_FOUND, categoryName));
            RecruitDto recruitDto = dto.getRecruitDto();
            Recruit savedRecruit = recruitRepository.save(recruitDto.toEntity(category, user));
            responseDtoList.add(new BookmarkResponseDto(savedRecruit.getId(), categoryName,
                    new RecruitDto(savedRecruit), getTagsDtoByBookmark(savedRecruit)));
        }

        return responseDtoList;
    }

    @Transactional
    public List<BookmarkResponseDto> unbookmark(User user, Long categoryId, Long bookmarkId) {
        if (!categoryService.existsByUserAndId(user, categoryId)) {
            throw new BookmarkException(FAILED_DELETE_BOOKMARK, CATEGORY_ID_NOT_FOUND, categoryId);
        }

        Recruit bookmark = findById(user, bookmarkId)
                .orElseThrow(() -> new BookmarkException(FAILED_DELETE_BOOKMARK, BOOKMARK_ID_NOT_FOUND, bookmarkId));
        Category category = bookmark.getCategory();

        if (!category.getId().equals(categoryId)) {
            throw new BookmarkException(FAILED_DELETE_BOOKMARK, CATEGORY_ID_NOT_FOUND, categoryId);
        }

        recruitTagRepository.deleteAllInBatch(bookmark.getRecruitTagList());
        recruitRepository.delete(bookmark);
        category.getRecruits().remove(bookmark);
        return toBookmarkResponseDtoList(category.getName(), category.getRecruits());
    }

    public List<BookmarkResponseDto> findAllByCategory(User user, Long categoryId) {
        Category category = categoryService.findByUserAndId(user, categoryId,
                () -> new BookmarkException(FAILED_GET_BOOKMARKS, CATEGORY_ID_NOT_FOUND, categoryId));
        List<Recruit> recruits = category.getRecruits();
        return toBookmarkResponseDtoList(category.getName(), recruits);
    }

    private List<BookmarkResponseDto> toBookmarkResponseDtoList(String categoryName, List<Recruit> recruits) {
        return recruits
                .stream()
                .map(recruit -> new BookmarkResponseDto(recruit.getId(), categoryName,
                        new RecruitDto(recruit), getTagsDtoByBookmark(recruit)))
                .toList();
    }

    public Optional<Recruit> findById(User user, Long bookmarkId) {
        return Optional.ofNullable(recruitRepository.findByUserAndId(user, bookmarkId));
    }

    private List<TagDto> getTagsDtoByBookmark(Recruit bookmark) {
        return bookmark.getRecruitTagList()
                .stream()
                .map(RecruitTag::getTag)
                .map(TagDto::new)
                .toList();
    }
}
