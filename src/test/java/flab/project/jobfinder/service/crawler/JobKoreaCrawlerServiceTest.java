package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.config.jobkorea.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.form.DetailedSearchDto;
import flab.project.jobfinder.service.crawler.generator.JobKoreaQueryParamGenerator;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static flab.project.jobfinder.enums.Location.BUNDANG;
import static flab.project.jobfinder.enums.Location.GANGNAM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobKoreaCrawlerServiceTest {

    @InjectMocks
    JobKoreaCrawlerService jobKoreaCrawlerService;

    @Mock
    JobKoreaPropertiesConfig jobKoreaPropertiesConfig;

    @Mock
    JobKoreaQueryParamGenerator paramGenerator;

    @Test
    void 크롤링_정상_작동_테스트() {
        DetailedSearchDto dto = DetailedSearchDto.builder().searchText("spring").location(List.of(GANGNAM, BUNDANG)).build();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>(Map.of(
                "stext", List.of("웹 서비스"),
                "local", List.of("I010", "B150")));
        when(paramGenerator.toQueryParams(dto, 1)).thenReturn(map);
        when(jobKoreaPropertiesConfig.getSearchUrl()).thenReturn("https://www.jobkorea.co.kr/Search/");

        Document result = jobKoreaCrawlerService.crawl(dto, 1);

        assertThat(result).isNotNull();
        assertThat(result.connection().response().statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}