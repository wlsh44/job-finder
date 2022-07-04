package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.user.User;
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

    public Recruit bookmarkRecruit(User user, RecruitDto dto) {
        return recruitRepository.save(dto.toEntity());
    }
}
