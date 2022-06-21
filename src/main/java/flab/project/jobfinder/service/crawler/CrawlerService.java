package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.dto.form.DetailedSearchDto;
import org.jsoup.nodes.Document;

public interface CrawlerService {
    Document crawl(DetailedSearchDto dto, int pageNum);
}
