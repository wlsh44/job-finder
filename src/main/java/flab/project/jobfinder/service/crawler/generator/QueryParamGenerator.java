package flab.project.jobfinder.service.crawler.generator;

import flab.project.jobfinder.dto.DetailedSearchDto;

public interface QueryParamGenerator {
    String toQueryParams(DetailedSearchDto dto);
}
