package flab.project.jobfinder.dto.bookmark;

import flab.project.jobfinder.dto.recruit.RecruitDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BookmarkResponseDto {
    private Long id;
    private RecruitDto recruitDto;
    private List<TagDto> tagList;
}
