package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.CategoryResponseDto;
import flab.project.jobfinder.dto.bookmark.NewCategoryRequestDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.CategoryNotFoundException;
import flab.project.jobfinder.exception.bookmark.CreateCategoryFailedException;
import flab.project.jobfinder.repository.CategoryRepository;
import flab.project.jobfinder.repository.RecruitTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static flab.project.jobfinder.enums.exception.CategoryErrorCode.ALREADY_EXISTS_CATEGORY;

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
            throw new CreateCategoryFailedException(dto, ALREADY_EXISTS_CATEGORY);
        }

        categoryRepository.save(dto.toEntity(user));
        return findAllByUser(user);
    }

    @Transactional
    public List<CategoryResponseDto> delete(User user, Long categoryId) {
        Category category = categoryRepository.findByUserAndId(user, categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        category.getRecruits()
                .forEach(recruit -> recruitTagRepository.deleteAllInBatch(recruit.getRecruitTagList()));
        categoryRepository.deleteById(categoryId);
        return findAllByUser(user);
    }

    public boolean existsByUserAndId(User user, Long categoryId) {
        return categoryRepository.existsByUserAndId(user, categoryId);
    }

    public Category findByUserAndName(User user, String name) {
        return categoryRepository.findByUserAndName(user, name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
    }

    public Category findByUserAndId(User user, Long id) {
        return categoryRepository.findByUserAndId(user, id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
