package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.dto.DetailedSearchDto;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Stream;

import static flab.project.jobfinder.enums.CareerType.*;
import static flab.project.jobfinder.enums.JobType.*;
import static flab.project.jobfinder.enums.Location.*;
import static flab.project.jobfinder.enums.PayType.*;
import static org.assertj.core.api.Assertions.assertThat;

class JobKoreaCrawlerServiceTest {

    JobKoreaCrawlerService jobKoreaCrawlerService = new JobKoreaCrawlerService();

    @Test
    void 크롤링_정상_작동_테스트() {
        DetailedSearchDto dto = DetailedSearchDto.builder().searchText("spring").build();

        Document result = jobKoreaCrawlerService.crawling(dto);

        assertThat(result).isNotNull();
        assertThat(result.connection().response().statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("detailedSearchQueryParams 메서드 테스트")
    class detailedSearchQueryParamsTest {

        private Stream<Arguments> provideDto() {
            return Stream.of(
                    Arguments.of(DetailedSearchDto.builder().build(), ""),
                    Arguments.of(DetailedSearchDto.builder().searchText("spring").build(), "stext=spring"),
                    Arguments.of(DetailedSearchDto.builder().searchText("spring boot")
                            .career(new DetailedSearchDto.Career(JUNIOR, null, "2")).build()
                            , "stext=spring+boot&careerType=1&careerMax=2"),
                    Arguments.of(DetailedSearchDto.builder().searchText("웹 서비스")
                            .location(List.of(GANGNAM, BUNDANG))
                            .pay(new DetailedSearchDto.Pay(ANNUAL, "4000", null)).build()
                            , "stext=%EC%9B%B9+%EC%84%9C%EB%B9%84%EC%8A%A4&local=I010%2CB150&payType=1&payMin=4000"),
                    Arguments.of(DetailedSearchDto.builder().searchText("react 웹 프런트")
                            .location(List.of(DONGDAEMUN, EUNPYEONG, YONGSAN))
                            .jobType(List.of(MILITARY)).build()
                            , "stext=react+%EC%9B%B9+%ED%94%84%EB%9F%B0%ED%8A%B8&local=I110%2CI220%2CI210&jobtype=9")
            );
        }

        @ParameterizedTest(name = "{index} => {1}")
        @MethodSource("provideDto")
        @DisplayName("정상 url 리턴")
        void 정상_url_리턴(DetailedSearchDto dto, String expected) {
            String result = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, "detailedSearchQueryParams", dto);

            assertThat(result).isEqualTo(expected);
        }
    }
}