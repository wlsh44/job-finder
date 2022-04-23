package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.service.crawler.generator.JobKoreaQueryParamGenerator;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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
        String givenText = "spring";
        DetailedSearchDto dto = DetailedSearchDto.builder().searchText(givenText).build();

        when(paramGenerator.toQueryParams(dto, 1)).thenReturn("stext=" + givenText);
        when(jobKoreaPropertiesConfig.getUrl()).thenReturn("https://www.jobkorea.co.kr/Search/?");

        Document result = jobKoreaCrawlerService.crawl(dto);

        assertThat(result).isNotNull();
        assertThat(result.connection().response().statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}