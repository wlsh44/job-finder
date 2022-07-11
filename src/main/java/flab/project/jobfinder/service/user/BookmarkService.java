package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.BookmarkNotFoundException;
import flab.project.jobfinder.exception.bookmark.CategoryNotFoundException;
import flab.project.jobfinder.exception.bookmark.CreateBookmarkFailedException;
import flab.project.jobfinder.exception.bookmark.CreateCategoryFailedException;
import flab.project.jobfinder.repository.CategoryRepository;
import flab.project.jobfinder.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static flab.project.jobfinder.enums.exception.CreateBookmarkFailedErrorCode.REQUIRED_AT_LEAST_ONE_CATEGORY;
import static flab.project.jobfinder.enums.exception.CreateCategoryFailedErrorCode.ALREADY_EXISTS_CATEGORY;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final RecruitRepository recruitRepository;
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> findCategoriesByUser(User user) {
        List<Category> categoryList = categoryRepository.findAllByUser(user);
        return categoryList.stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CategoryDto> createCategory(User user, NewCategoryRequestDto dto) {
        if (categoryRepository.existsByUserAndName(user, dto.getName())) {
            throw new CreateCategoryFailedException(dto, ALREADY_EXISTS_CATEGORY);
        }

        categoryRepository.save(dto.toEntity(user));
        return findCategoriesByUser(user);
    }

    @Transactional
    public CategoryDto deleteCategory(User user, Long categoryId) {
        if (!categoryRepository.existsByUserAndId(user, categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }

        Category category = findCategoryByUserAndId(user, categoryId);
        categoryRepository.delete(category);
        return new CategoryDto(category);
    }

    public Category findCategoryByUserAndName(User user, String name) {
        return categoryRepository.findByUserAndName(user, name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
    }

    public Category findCategoryByUserAndId(User user, Long id) {
        return categoryRepository.findByUserAndId(user, id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

//    public BookmarkResponseDto bookmarkRecruit(User user, Long categoryId, NewBookmarkRequestDto dto) {
//        Category category = findCategoryByUserAndId(user, categoryId);
//        RecruitDto recruitDto = dto.getRecruitDto();
//        Recruit recruit = recruitRepository.save(recruitDto.toEntity(category));
//
//        return new BookmarkResponseDto(recruit.getId(), recruitDto);
//    }

    @Transactional
    public List<BookmarkResponseDto> bookmarkRecruit(User user, NewBookmarkRequestDto2 dto) {
        List<String> categoryList = dto.getCategoryList();
        if (categoryList.isEmpty()) {
            throw new CreateBookmarkFailedException(dto, REQUIRED_AT_LEAST_ONE_CATEGORY);
        }
        List<BookmarkResponseDto> responseDtoList = new ArrayList<>();

        for (String categoryName : categoryList) {
            Category category = findCategoryByUserAndName(user, categoryName);
            RecruitDto recruitDto = dto.getRecruitDto();
            Recruit savedRecruit = recruitRepository.save(recruitDto.toEntity(category));
            responseDtoList.add(new BookmarkResponseDto(savedRecruit.getId(), categoryName, new RecruitDto(savedRecruit)));
        }

        return responseDtoList;
    }

    @Transactional
    public List<BookmarkResponseDto> unbookmarkRecruit(User user, Long categoryId, Long bookmarkId) {
        if (!categoryRepository.existsByUserAndId(user, categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }

        Recruit recruit = recruitRepository.findById(bookmarkId).
                orElseThrow(() -> new BookmarkNotFoundException(bookmarkId));
        Category category = recruit.getCategory();

        if (!category.getId().equals(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }

        category.getRecruits().remove(recruit);
        return toBookmarkResponseDtoList(category.getName(), category.getRecruits());
    }

    public List<BookmarkResponseDto> findAllBookmarksByCategory(User user, Long categoryId) {
        Category category = findCategoryByUserAndId(user, categoryId);
        List<Recruit> recruits = category.getRecruits();
        return toBookmarkResponseDtoList(category.getName(), recruits);
    }

    private List<BookmarkResponseDto> toBookmarkResponseDtoList(String categoryName, List<Recruit> recruits) {
        return recruits
                .stream()
                .map(recruit -> new BookmarkResponseDto(recruit.getId(), categoryName, new RecruitDto(recruit)))
                .toList();
    }
}
