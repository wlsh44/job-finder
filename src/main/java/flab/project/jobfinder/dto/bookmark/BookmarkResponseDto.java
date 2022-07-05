package flab.project.jobfinder.dto.bookmark;

import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.entity.recruit.Recruit;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookmarkResponseDto {
    private Long id;
    private RecruitDto recruitDto;
}
