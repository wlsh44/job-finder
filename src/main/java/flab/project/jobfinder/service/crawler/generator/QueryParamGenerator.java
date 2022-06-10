package flab.project.jobfinder.service.crawler.generator;

import flab.project.jobfinder.dto.DetailedSearchDto;
import org.springframework.util.MultiValueMap;

public interface QueryParamGenerator {
    MultiValueMap<String, String> toQueryParams(DetailedSearchDto dto, int pageNum);
}
