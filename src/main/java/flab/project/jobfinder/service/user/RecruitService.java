package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.recruit.RecruitTag;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.*;
import flab.project.jobfinder.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static flab.project.jobfinder.enums.bookmark.BookmarkResponseCode.*;
import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;

    @Transactional
    public List<BookmarkResponseDto> bookmark(User user, RecruitDto recruitDto, List<Category> categoryList) {
        if (categoryList.isEmpty()) {
            throw new BookmarkException(FAILED_CREATE_BOOKMARK, REQUIRED_AT_LEAST_ONE_CATEGORY);
        }

        return categoryList.stream()
                .map(category -> recruitRepository.save(recruitDto.toEntity(category, user)))
                .map(recruit -> new BookmarkResponseDto(recruit.getId(), recruit.getCategory().getName(),
                        new RecruitDto(recruit), null))
                .collect(Collectors.toList());
    }

    @Transactional
    public long unbookmark(Recruit bookmark) {
        Category category = bookmark.getCategory();
        recruitRepository.delete(bookmark);
        category.getRecruits().remove(bookmark);
        return bookmark.getId();
    }

    public List<BookmarkResponseDto> findAllByCategory(User user, Category category) {
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

    public Optional<Recruit> findByCategoryIdAndBookmarkId(User user, Long categoryId, Long bookmarkId) {
        return Optional.ofNullable(recruitRepository.findByUserAndCategory_IdAndId(user, categoryId, bookmarkId));
    }

    private List<TagDto> getTagsDtoByBookmark(Recruit bookmark) {
        return bookmark.getRecruitTagList()
                .stream()
                .map(RecruitTag::getTag)
                .map(TagDto::new)
                .toList();
    }
}
