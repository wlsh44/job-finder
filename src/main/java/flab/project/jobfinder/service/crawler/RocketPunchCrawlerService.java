package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.config.RocketPunchPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.exception.CrawlFailedException;
import flab.project.jobfinder.service.crawler.generator.QueryParamGenerator;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class RocketPunchCrawlerService implements CrawlerService {

    private final QueryParamGenerator paramGenerator;
    private final RocketPunchPropertiesConfig config;

    @Autowired
    public RocketPunchCrawlerService(@Qualifier("rocketPunchQueryParamGenerator") QueryParamGenerator paramGenerator, RocketPunchPropertiesConfig config) {
        this.paramGenerator = paramGenerator;
        this.config = config;
    }

    @Override
    public Document crawl(DetailedSearchDto dto, int pageNum) {
        String url = config.getSearchUrl() + paramGenerator.toQueryParams(dto, pageNum);

        log.info("parsing url = {}", url);
        try {
            Document doc = Jsoup.connect(url).get();

            return doc;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new CrawlFailedException("서버 연결에 실패했습니다.");
        }
    }
}