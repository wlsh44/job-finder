package flab.project.jobfinder.service.perser;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.ParseDto;

public interface ParserService {
    ParseDto parse(DetailedSearchDto dto);
}
