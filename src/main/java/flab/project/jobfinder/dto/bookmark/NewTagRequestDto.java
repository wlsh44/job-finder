package flab.project.jobfinder.dto.bookmark;

import flab.project.jobfinder.entity.recruit.Tag;
import flab.project.jobfinder.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class NewTagRequestDto {
    @NotBlank
    @Size(max = 20)
    private String name;

    public Tag toEntity(User user) {
        return Tag.builder()
                .name(name)
                .user(user)
                .build();
    }
}
