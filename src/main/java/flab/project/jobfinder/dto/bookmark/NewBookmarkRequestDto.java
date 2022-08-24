package flab.project.jobfinder.dto.bookmark;

import flab.project.jobfinder.dto.recruit.RecruitDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewBookmarkRequestDto {
    @NotNull
    private List<String> categoryList;
    @NotNull
    private RecruitDto recruitDto;
}
