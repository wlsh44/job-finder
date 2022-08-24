package flab.project.jobfinder.service.jobfind.parser;

import flab.project.jobfinder.dto.recruit.RecruitDto;
import org.jsoup.select.Elements;

import java.util.List;

public interface ParserService {
    List<RecruitDto> parse(Elements recruits);
}
