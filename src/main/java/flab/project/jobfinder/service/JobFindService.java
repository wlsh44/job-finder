package flab.project.jobfinder.service;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitPageDto;

public interface JobFindService {
    RecruitPageDto findJobByPage(DetailedSearchDto dto, int page);
}
