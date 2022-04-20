package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.service.crawler.generator.JobKoreaQueryParamGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobKoreaCrawlerService implements CrawlerService {

    private final JobKoreaQueryParamGenerator paramGenerator;
    private final JobKoreaPropertiesConfig config;

    @Override
    public Document crawl(DetailedSearchDto dto) {
        String url = config.getUrl() + paramGenerator.toQueryParams(dto);

        try {
            Document doc = Jsoup.connect(url).get();

            return doc;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
