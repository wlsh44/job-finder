package flab.project.jobfinder.service.perser.handler;

import flab.project.jobfinder.dto.ParseDto;
import org.jsoup.select.Elements;

import java.util.List;

public interface ParseHandler {
    List<ParseDto> getParseDtoList(Elements recruits);
}
