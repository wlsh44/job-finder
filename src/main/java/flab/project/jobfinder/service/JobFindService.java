package flab.project.jobfinder.service;

import flab.project.jobfinder.dto.form.DetailedSearchDto;
import flab.project.jobfinder.dto.page.RecruitPageDto;
import flab.project.jobfinder.enums.Platform;

public interface JobFindService {
    RecruitPageDto findJobByPage(DetailedSearchDto dto, int page);
    Platform getPlatform();
}
