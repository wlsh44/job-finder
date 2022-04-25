package flab.project.jobfinder.service;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.ParseDto;
import flab.project.jobfinder.service.crawler.CrawlerService;
import flab.project.jobfinder.service.parser.ParserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class JobKoreaJobFindService implements JobFindService {

    private final CrawlerService jobKoreaCrawlerService;
    private final ParserService jobKoreaParserService;
    private final JobKoreaPropertiesConfig config;

    @Override
    public List<ParseDto> find(DetailedSearchDto dto) {
        List<ParseDto> parseDtoList = new ArrayList<>();
        Elements recruits;
        int pageNum = 1;

        do {
            Document doc = jobKoreaCrawlerService.crawl(dto, pageNum++);
            recruits = doc.select(config.getSelector());
            List<ParseDto> list = jobKoreaParserService.parse(recruits);
            parseDtoList.addAll(list);

            log.info("page {} recruits size: {}", pageNum - 1, list.size());
        } while (recruits.size() > 0);
        return parseDtoList;
    }
}
