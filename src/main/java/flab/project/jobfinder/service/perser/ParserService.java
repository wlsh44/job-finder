package flab.project.jobfinder.service.perser;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.ParseDto;

import java.util.List;

public interface ParserService {
    List<ParseDto> parse(DetailedSearchDto dto);
}
