package flab.project.jobfinder.service.perser;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.ParseDto;
import flab.project.jobfinder.service.crawler.CrawlerService;
import flab.project.jobfinder.service.perser.handler.ParseHandler;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class JobKoreaParserService implements ParserService {

    private final CrawlerService jobKoreaCrawlerService;
    private final ParseHandler jobKoreaParseHandler;
    private final JobKoreaPropertiesConfig config;

    @Override
    public List<ParseDto> parse(DetailedSearchDto dto) {
        List<ParseDto> parseDtoList = new ArrayList<>();
        Elements recruits;
        int pageNum = 1;

        do {
            Document doc = jobKoreaCrawlerService.crawl(dto, pageNum++);
            recruits = doc.select(config.getSelector());
            List<ParseDto> list = jobKoreaParseHandler.getParseDtoList(recruits);

            parseDtoList.addAll(list);
        } while (recruits.size() > 0);

        return parseDtoList;
    }
}
