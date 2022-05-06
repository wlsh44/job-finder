package flab.project.jobfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RecruitPageDto {
    private List<RecruitDto> list;
    private int totalPage;
    private int startPage;
}
