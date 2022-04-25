package flab.project.jobfinder.service.perser;

import flab.project.jobfinder.dto.ParseDto;
import org.jsoup.select.Elements;

import java.util.List;

public interface ParserService {
    List<ParseDto> parse(Elements recruits);
}
