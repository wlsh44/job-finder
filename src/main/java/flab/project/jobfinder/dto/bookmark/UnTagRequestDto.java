package flab.project.jobfinder.dto.bookmark;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UnTagRequestDto {
    @NotBlank
    private String tagId;
}
