package flab.project.jobfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitPageDto {
    private List<RecruitDto> list;
    private int totalPage;
    private int startPage;
}
