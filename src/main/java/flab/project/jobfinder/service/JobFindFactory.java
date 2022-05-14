package flab.project.jobfinder.service;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitPageDto;
import flab.project.jobfinder.enums.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JobFindFactory {

    private final Map<Platform, JobFindService> jobFindServiceMap;

    @Autowired
    public JobFindFactory(List<JobFindService> jobFindServices) {
        jobFindServiceMap = new HashMap<>();

        for (JobFindService jobFindService : jobFindServices) {
            jobFindServiceMap.put(jobFindService.getPlatform(), jobFindService);
        }
    }

    public RecruitPageDto getRecruitPageDto(DetailedSearchDto dto, Integer currentPage) {
        JobFindService jobFindService = jobFindServiceMap.get(dto.getPlatform());
        return jobFindService.findJobByPage(dto, currentPage);
    }
}
