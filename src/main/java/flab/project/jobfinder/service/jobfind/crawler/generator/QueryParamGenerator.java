package flab.project.jobfinder.service.jobfind.crawler.generator;

import flab.project.jobfinder.dto.form.DetailedSearchDto;
import org.springframework.util.MultiValueMap;

public interface QueryParamGenerator {
    MultiValueMap<String, String> toQueryParams(DetailedSearchDto dto, int pageNum);
}
