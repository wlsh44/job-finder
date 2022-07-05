package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.BookmarkNotFoundException;
import flab.project.jobfinder.exception.bookmark.CategoryNotFoundException;
import flab.project.jobfinder.exception.bookmark.CreateCategoryFailedException;
import flab.project.jobfinder.repository.CategoryRepository;
import flab.project.jobfinder.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static flab.project.jobfinder.enums.exception.CreateCategoryFailedErrorCode.ALREADY_EXISTS_CATEGORY;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final RecruitRepository recruitRepository;
    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> findCategoriesByUser(User user) {
        List<Category> categoryList = categoryRepository.findAllByUser(user);
        return categoryList.stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    public CategoryResponseDto createCategory(User user, NewCategoryRequestDto dto) {
        if (categoryRepository.existsByUserAndName(user, dto.getName())) {
            throw new CreateCategoryFailedException(dto, ALREADY_EXISTS_CATEGORY);
        }

        Category category = categoryRepository.save(dto.toEntity(user));
        return new CategoryResponseDto(category);
    }

    public CategoryResponseDto deleteCategory(User user, DeleteCategoryRequestDto dto) {
        if (!categoryRepository.existsByUserAndName(user, dto.getName())) {
            throw new CategoryNotFoundException(dto.getName());
        }

        Category category = findCategoryByUserAndName(user, dto.getName());
        categoryRepository.delete(category);
        return new CategoryResponseDto(category);
    }

    public Category findCategoryByUserAndName(User user, String name) {
        return categoryRepository.findCategoryByUserAndName(user, name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
    }

    public BookmarkResponseDto bookmarkedRecruit(User user, NewBookmarkRequestDto dto) {
        Category category = findCategoryByUserAndName(user, dto.getCategoryName());
        RecruitDto recruitDto = dto.getRecruitDto();
        Recruit recruit = recruitRepository.save(recruitDto.toEntity(category));

        return new BookmarkResponseDto(recruit.getId(), recruitDto);
    }

    public BookmarkResponseDto unbookmarkedRecruit(User user, UnbookmarkRequestDto dto) {
        Category category = findCategoryByUserAndName(user, dto.getCategoryName());
        Recruit removeRecruit = category.getRecruits()
                .stream()
                .filter(recruit -> recruit.getId().equals(dto.getBookmarkId()))
                .findAny()
                .orElseThrow(() -> new BookmarkNotFoundException(dto.getBookmarkId()));
        recruitRepository.delete(removeRecruit);

        RecruitDto recruitDto = new RecruitDto(removeRecruit);
        return new BookmarkResponseDto(removeRecruit.getId(), recruitDto);
    }

    public List<BookmarkResponseDto> findAllBookmarksByCategory(User user, BookmarkListRequestDto dto) {
        Category category = findCategoryByUserAndName(user, dto.getCategoryName());
        List<Recruit> recruits = category.getRecruits();
        return recruits
                .stream()
                .map(recruit -> new BookmarkResponseDto(recruit.getId(), new RecruitDto(recruit)))
                .toList();
    }
}
