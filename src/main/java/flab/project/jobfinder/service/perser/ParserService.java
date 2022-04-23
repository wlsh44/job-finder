package flab.project.jobfinder.service.perser;

import flab.project.jobfinder.dto.ParseDto;
import org.jsoup.nodes.Document;

public interface ParserService {
    ParseDto parse(Document doc);
}
