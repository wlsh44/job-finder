package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.config.RocketPunchPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.exception.CrawlFailedException;
import flab.project.jobfinder.service.crawler.generator.QueryParamGenerator;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        Set<Map.Entry<String, List<String>>> queryParams = rocketPunchQueryParamGenerator
                                                            .toQueryParams(dto, pageNum)
                                                            .entrySet();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);

        log.info("url = {}", url);
        log.info("queryParams = {}", queryParams);

        queryParams.forEach(entry -> entry.getValue()
                    .forEach(param -> uriBuilder.queryParam(entry.getKey(), param)));
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
