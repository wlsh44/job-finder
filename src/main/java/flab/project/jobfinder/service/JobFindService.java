package flab.project.jobfinder.service;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitPageDto;
import flab.project.jobfinder.enums.Platform;

public interface JobFindService {
    RecruitPageDto findJobByPage(DetailedSearchDto dto, int page);
    Platform getPlatform();
}
