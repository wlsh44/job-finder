package flab.project.jobfinder.dto.bookmark;

import flab.project.jobfinder.dto.recruit.RecruitDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewBookmarkRequestDto2 {
    private List<String> categoryList;
    private RecruitDto recruitDto;
}
