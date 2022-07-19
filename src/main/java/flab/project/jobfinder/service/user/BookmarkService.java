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
import java.util.function.Supplier;

import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.*;

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
            throw new CreateBookmarkFailedException(dto, REQUIRED_AT_LEAST_ONE_CATEGORY);
        }
        List<BookmarkResponseDto> responseDtoList = new ArrayList<>();

        for (String categoryName : categoryList) {
            Category category = categoryService.findByUserAndName(user, categoryName);
            RecruitDto recruitDto = dto.getRecruitDto();
            Recruit savedRecruit = recruitRepository.save(recruitDto.toEntity(category));
            responseDtoList.add(new BookmarkResponseDto(savedRecruit.getId(), categoryName,
                    new RecruitDto(savedRecruit), getTagsDtoByBookmark(savedRecruit)));
        }

        return responseDtoList;
    }

    @Transactional
    public List<BookmarkResponseDto> unbookmark(User user, Long categoryId, Long bookmarkId) {
        if (!categoryService.existsByUserAndId(user, categoryId)) {
            throw new FindCategoryFailedException(categoryId);
        }

        Recruit bookmark = getBookmarkById(bookmarkId, () -> new BookmarkNotFoundException(bookmarkId));
        Category category = bookmark.getCategory();

        if (!category.getId().equals(categoryId)) {
            throw new FindCategoryFailedException(categoryId);
        }

        recruitTagRepository.deleteAllInBatch(bookmark.getRecruitTagList());
        recruitRepository.delete(bookmark);
        category.getRecruits().remove(bookmark);
        return toBookmarkResponseDtoList(category.getName(), category.getRecruits());
    }

    public List<BookmarkResponseDto> findAllByCategory(User user, Long categoryId) {
        Category category = categoryService.findByUserAndId(user, categoryId);
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

    private Recruit getBookmarkById(Long bookmarkId, Supplier<? extends RuntimeException> e) {
        return recruitRepository.findById(bookmarkId)
                .orElseThrow(e);
    }

    private List<TagDto> getTagsDtoByBookmark(Recruit bookmark) {
        return bookmark.getRecruitTagList()
                .stream()
                .map(RecruitTag::getTag)
                .map(TagDto::new)
                .toList();
    }
}
