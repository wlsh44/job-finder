package flab.project.jobfinder.service.jobfind.crawler;

import flab.project.jobfinder.config.rocketpunch.RocketPunchPropertiesConfig;
import flab.project.jobfinder.dto.form.DetailedSearchDto;
import flab.project.jobfinder.exception.CrawlFailedException;
import flab.project.jobfinder.service.jobfind.crawler.generator.QueryParamGenerator;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class RocketPunchCrawlerService implements CrawlerService {

    private final QueryParamGenerator rocketPunchQueryParamGenerator;
    private final RocketPunchPropertiesConfig config;
    private final WebClient webClient;

    @Override
    public Document crawl(DetailedSearchDto dto, int pageNum) {
        URI uri = makeUri(dto, pageNum);

        try {
            String htmlTemplate = webClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(RocketPunchResponseDto.class)
                    .block()
                    .getData()
                    .getTemplate();
            return Jsoup.parse(htmlTemplate);
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            throw new CrawlFailedException("서버 연결에 실패했습니다.");
        }
    }

    private URI makeUri(DetailedSearchDto dto, int pageNum) {
        String url = config.getSearchUrl();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
        rocketPunchQueryParamGenerator.toQueryParams(dto, pageNum)
                                        .forEach((key, value) -> value
                                                .forEach(param -> uriBuilder.queryParam(key, param)));

        log.info("url = {}", url);

        return uriBuilder.build().encode().toUri();
    }

    @Data
    static class RocketPunchResponseDto {
        private Content data;

        @Data
        static class Content {
            private String template;
        }
    }
}
