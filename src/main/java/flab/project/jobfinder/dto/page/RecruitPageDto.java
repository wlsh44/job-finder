package flab.project.jobfinder.dto.page;

import flab.project.jobfinder.dto.recruit.RecruitDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RecruitPageDto {
    private List<RecruitDto> recruitDtoList;
    private PageDto pageDto;
}
