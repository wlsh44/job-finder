package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.exception.CrawlFailedException;
import flab.project.jobfinder.service.crawler.generator.QueryParamGenerator;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class JobKoreaCrawlerService implements CrawlerService {

    private final QueryParamGenerator paramGenerator;
    private final JobKoreaPropertiesConfig config;

    @Autowired
    public JobKoreaCrawlerService(@Qualifier("jobKoreaQueryParamGenerator") QueryParamGenerator paramGenerator, JobKoreaPropertiesConfig config) {
        this.paramGenerator = paramGenerator;
        this.config = config;
    }

    @Override
    public Document crawl(DetailedSearchDto dto, int pageNum) {
        String url = config.getSearchUrl();
        Map<String, String> queryParams = paramGenerator.toQueryParams(dto, pageNum).toSingleValueMap();

        try {
            log.info("url = {}", url);
            log.info("queryParam = {}", queryParams);
            return  Jsoup.connect(url)
                    .data(queryParams)
                    .timeout(5000)
                    .get();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new CrawlFailedException("서버 연결에 실패했습니다.");
        }
    }
}
