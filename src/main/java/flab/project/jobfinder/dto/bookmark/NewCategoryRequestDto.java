package flab.project.jobfinder.dto.bookmark;

import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class NewCategoryRequestDto {
    @NotBlank
    private String name;

    public Category toEntity(User user) {
        if (user == null) {
            throw new NullPointerException();
        }
        return Category.builder()
                .user(user)
                .name(name)
                .build();
    }
}
