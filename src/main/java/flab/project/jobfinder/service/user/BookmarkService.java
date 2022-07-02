package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.BookmarkRequestDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.repository.CategoryRepository;
import flab.project.jobfinder.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final RecruitRepository recruitRepository;
    private final CategoryRepository categoryRepository;

    public List<Category> findCategoryByUser(User user) {
        return categoryRepository.findByUser(user);
    }

    public Category createCategory(User user, BookmarkRequestDto dto) {
        Category category = Category.builder()
                .name(dto.getName())
                .user(user)
                .build();
        return categoryRepository.save(category);
    }
}
