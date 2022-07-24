package flab.project.jobfinder.dto.bookmark;

import flab.project.jobfinder.entity.recruit.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CategoryResponseDto {
    private Long id;
    private String name;

    public CategoryResponseDto(Category category) {
        if (category == null) {
            throw new IllegalArgumentException();
        }
        this.id = category.getId();
        this.name = category.getName();
    }
}
