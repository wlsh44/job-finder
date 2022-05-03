package flab.project.jobfinder.service;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.ParseDto;

import java.util.List;

public interface JobFindService {
    List<ParseDto> findJob(DetailedSearchDto dto);
}
