package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.CategoryResponseDto;
import flab.project.jobfinder.dto.bookmark.NewCategoryRequestDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.CategoryException;
import flab.project.jobfinder.repository.CategoryRepository;
import flab.project.jobfinder.repository.RecruitTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static flab.project.jobfinder.enums.bookmark.CategoryResponseCode.*;
import static flab.project.jobfinder.enums.exception.CategoryErrorCode.*;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final RecruitTagRepository recruitTagRepository;

    public List<CategoryResponseDto> findAllByUser(User user) {
        List<Category> categoryList = categoryRepository.findAllByUser(user);
        return categoryList.stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CategoryResponseDto> create(User user, NewCategoryRequestDto dto) {
        if (categoryRepository.existsByUserAndName(user, dto.getName())) {
            throw new CategoryException(FAILED_CREATE_CATEGORY, ALREADY_EXISTS_CATEGORY);
        }

        categoryRepository.save(dto.toEntity(user));
        return findAllByUser(user);
    }

    @Transactional
    public List<CategoryResponseDto> delete(User user, Long categoryId) {
        Category category = categoryRepository.findByUserAndId(user, categoryId)
                .orElseThrow(() -> new CategoryException(FAILED_DELETE_CATEGORY, CATEGORY_ID_NOT_FOUND, categoryId));

        category.getRecruits()
                .forEach(recruit -> recruitTagRepository.deleteAllInBatch(recruit.getRecruitTagList()));
        categoryRepository.deleteById(categoryId);
        return findAllByUser(user);
    }

    public boolean existsByUserAndId(User user, Long categoryId) {
        return categoryRepository.existsByUserAndId(user, categoryId);
    }

    public Category findByUserAndName(User user, String name, Supplier<? extends RuntimeException> e) {
        return categoryRepository.findByUserAndName(user, name)
                .orElseThrow(e);
    }

    public Category findByUserAndId(User user, Long id, Supplier<? extends RuntimeException> e) {
        return categoryRepository.findByUserAndId(user, id)
                .orElseThrow(e);
    }
}
