package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.dto.DetailedSearchDto;
import org.jsoup.nodes.Document;

public interface CrawlerService {
    Document crawling(DetailedSearchDto dto);
}
