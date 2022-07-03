package flab.project.jobfinder.dto.bookmark;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class DeleteCategoryRequestDto {
    @NotBlank
    private String name;
}
