package flab.project.jobfinder.dto.bookmark;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class UnTaggingRequestDto {
    @NotBlank
    @Positive
    private String tagId;
}
