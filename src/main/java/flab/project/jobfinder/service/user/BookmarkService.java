package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.*;
import flab.project.jobfinder.repository.CategoryRepository;
import flab.project.jobfinder.repository.RecruitRepository;
import flab.project.jobfinder.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.*;
import static flab.project.jobfinder.enums.exception.CategoryErrorCode.ALREADY_EXISTS_CATEGORY;
import static flab.project.jobfinder.enums.exception.TagErrorCode.ALREADY_EXISTS_TAG;
import static flab.project.jobfinder.enums.exception.TagErrorCode.TAG_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final RecruitRepository recruitRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public List<CategoryResponseDto> findCategoriesByUser(User user) {
        List<Category> categoryList = categoryRepository.findAllByUser(user);
        return categoryList.stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CategoryResponseDto> createCategory(User user, NewCategoryRequestDto dto) {
        if (categoryRepository.existsByUserAndName(user, dto.getName())) {
            throw new CreateCategoryFailedException(dto, ALREADY_EXISTS_CATEGORY);
        }

        categoryRepository.save(dto.toEntity(user));
        return findCategoriesByUser(user);
    }

    @Transactional
    public List<CategoryResponseDto> deleteCategory(User user, Long categoryId) {
        if (!categoryRepository.existsByUserAndId(user, categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }

        categoryRepository.deleteById(categoryId);
        return findCategoriesByUser(user);
    }

    private Category findCategoryByUserAndName(User user, String name) {
        return categoryRepository.findByUserAndName(user, name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
    }

    private Category findCategoryByUserAndId(User user, Long id) {
        return categoryRepository.findByUserAndId(user, id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Transactional
    public List<BookmarkResponseDto> bookmarkRecruit(User user, NewBookmarkRequestDto dto) {
        List<String> categoryList = dto.getCategoryList();
        if (categoryList.isEmpty()) {
            throw new CreateBookmarkFailedException(dto, REQUIRED_AT_LEAST_ONE_CATEGORY);
        }
        List<BookmarkResponseDto> responseDtoList = new ArrayList<>();

        for (String categoryName : categoryList) {
            Category category = findCategoryByUserAndName(user, categoryName);
            RecruitDto recruitDto = dto.getRecruitDto();
            Recruit savedRecruit = recruitRepository.save(recruitDto.toEntity(category));
            responseDtoList.add(new BookmarkResponseDto(savedRecruit.getId(), categoryName, new RecruitDto(savedRecruit), getTagsDtoByBookmark(savedRecruit)));
        }

        return responseDtoList;
    }

    @Transactional
    public List<BookmarkResponseDto> unbookmarkRecruit(User user, Long categoryId, Long bookmarkId) {
        if (!categoryRepository.existsByUserAndId(user, categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }

        Recruit recruit = recruitRepository.findById(bookmarkId)
                .orElseThrow(() -> new BookmarkNotFoundException(bookmarkId));
        Category category = recruit.getCategory();

        if (!category.getId().equals(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }

        recruitRepository.delete(recruit);
        category.getRecruits().remove(recruit);
        return toBookmarkResponseDtoList(category.getName(), category.getRecruits());
    }

    public List<BookmarkResponseDto> findAllBookmarksByCategory(User user, Long categoryId) {
        Category category = findCategoryByUserAndId(user, categoryId);
        List<Recruit> recruits = category.getRecruits();
        return toBookmarkResponseDtoList(category.getName(), recruits);
    }

    public List<BookmarkResponseDto> toBookmarkResponseDtoList(String categoryName, List<Recruit> recruits) {
        return recruits
                .stream()
                .map(recruit -> new BookmarkResponseDto(recruit.getId(), categoryName, new RecruitDto(recruit), getTagsDtoByBookmark(recruit)))
                .toList();
    }

    @Transactional
    public List<TagDto> createTag(User user, NewTagRequestDto dto) {
        if (tagRepository.existsByNameAndUser(dto.getName(), user)) {
            throw new CreateTagFailedException(dto, ALREADY_EXISTS_TAG);
        }

        tagRepository.save(dto.toEntity(user));
        return findTagsByUser(user);
    }

    public List<TagDto> findTagsByUser(User user) {
        return tagRepository.findTagsByUser(user)
                .stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TagDto> tagging(User user, Long bookmarkId, TaggingRequestDto dto) {
        Recruit bookmark = recruitRepository.findById(bookmarkId)
                .orElseThrow(() -> new TaggingFailedException(BOOKMARK_ID_NOT_FOUND, bookmarkId));
        List<String> newTagList = dto.getTagList();

        newTagList.stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseThrow(() -> new TaggingFailedException(TAG_NOT_FOUND, tagName)))
                .filter(tag -> !bookmark.getTags().contains(tag))
                .forEach(tag -> bookmark.getTags().add(tag));
        return getTagsDtoByBookmark(bookmark);
    }

    public List<TagDto> findTagByUser(User user) {
        return tagRepository.findTagsByUser(user)
                .stream()
                .map(TagDto::new).toList();
    }

    private List<TagDto> getTagsDtoByBookmark(Recruit bookmark) {
        return bookmark.getTags()
                .stream()
                .map(TagDto::new)
                .toList();
    }
}
