package flab.project.jobfinder.dto.bookmark;

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
}
