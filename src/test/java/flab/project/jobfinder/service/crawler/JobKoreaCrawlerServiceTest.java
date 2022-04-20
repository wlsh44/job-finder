package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.config.JobKoreaConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.service.crawler.generator.JobKoreaQueryParamGenerator;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties(value = JobKoreaConfig.class)
@TestPropertySource("classpath:application-test.properties")
class JobKoreaCrawlerServiceTest {

    @InjectMocks
    JobKoreaCrawlerService jobKoreaCrawlerService;

    @Mock
    JobKoreaConfig jobKoreaConfig;

    @Mock
    JobKoreaQueryParamGenerator paramGenerator;

    @Test
    void 크롤링_정상_작동_테스트() {
        String givenText = "spring";
        DetailedSearchDto dto = DetailedSearchDto.builder().searchText(givenText).build();

        when(paramGenerator.toQueryParams(dto)).thenReturn("stext=" + givenText);
        when(jobKoreaConfig.getUrl()).thenReturn("https://www.jobkorea.co.kr/Search/?");

        Document result = jobKoreaCrawlerService.crawl(dto);

        assertThat(result).isNotNull();
        assertThat(result.connection().response().statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}