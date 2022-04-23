package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
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
        String url = config.getUrl() + paramGenerator.toQueryParams(dto, pageNum);

        try {
            Document doc = Jsoup.connect(url).get();

            return doc;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
