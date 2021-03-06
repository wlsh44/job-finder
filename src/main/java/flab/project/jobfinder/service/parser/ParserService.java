package flab.project.jobfinder.service.parser;

import flab.project.jobfinder.dto.RecruitDto;
import org.jsoup.select.Elements;

import java.util.List;

public interface ParserService {
    List<RecruitDto> parse(Elements recruits);
}
