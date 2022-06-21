package flab.project.jobfinder.service.jobfind.crawler;

import flab.project.jobfinder.dto.form.DetailedSearchDto;
import org.jsoup.nodes.Document;

public interface CrawlerService {
    Document crawl(DetailedSearchDto dto, int pageNum);
}
