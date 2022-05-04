package flab.project.jobfinder.service;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitDto;

import java.util.List;

public interface JobFindService {
    List<RecruitDto> findJob(DetailedSearchDto dto);
}
