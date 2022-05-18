package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.config.RocketPunchPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.service.crawler.generator.RocketPunchQueryParamGenerator;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

import static flab.project.jobfinder.enums.Location.BUNDANG;
import static flab.project.jobfinder.enums.Location.GANGNAM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
class RocketPunchCrawlerServiceTest {

    RocketPunchCrawlerService rocketPunchCrawlerService;
    RocketPunchPropertiesConfig rocketPunchPropertiesConfig;
    RocketPunchQueryParamGenerator paramGenerator;
    WebClient webClient;

    @BeforeEach
    void init() {
        rocketPunchPropertiesConfig = mock(RocketPunchPropertiesConfig.class);
        paramGenerator = mock(RocketPunchQueryParamGenerator.class);
        webClient = WebClient.builder()
                .baseUrl("https://www.rocketpunch.com/api/jobs/template")
                .build();
        rocketPunchCrawlerService = new RocketPunchCrawlerService(paramGenerator, rocketPunchPropertiesConfig, webClient);
    }

    @Test
    void 크롤링_정상_작동_테스트() {
        DetailedSearchDto dto = DetailedSearchDto.builder().searchText("spring").location(List.of(GANGNAM, BUNDANG)).build();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>(Map.of(
                "keywords", List.of("웹 서비스"),
                "location", List.of("강남구", "분당구")));
        when(paramGenerator.toQueryParams(dto, 1)).thenReturn(map);
        when(rocketPunchPropertiesConfig.getSearchUrl()).thenReturn("https://www.rocketpunch.com/api/jobs/template");

        Document result = rocketPunchCrawlerService.crawl(dto, 1);

        assertThat(result).isNotNull();
        assertThat(result.connection().response().statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}