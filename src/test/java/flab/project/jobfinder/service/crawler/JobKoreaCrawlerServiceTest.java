package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.util.CareerType;
import flab.project.jobfinder.util.JobType;
import flab.project.jobfinder.util.Location;
import flab.project.jobfinder.util.PayType;
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

import static flab.project.jobfinder.util.CareerType.*;
import static flab.project.jobfinder.util.JobKoreaConst.JOBKOREA_URL;
import static flab.project.jobfinder.util.JobType.*;
import static flab.project.jobfinder.util.Location.*;
import static flab.project.jobfinder.util.PayType.*;
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
    @DisplayName("getSearchText 메서드 테스트")
    class getSearchTextTest {

        private Stream<Arguments> provideSearchText() {
            return Stream.of(
                    Arguments.of("spring", "stext=spring"),
                    Arguments.of("spring boot", "stext=spring+boot"),
                    Arguments.of("docker", "stext=docker")
            );
        }

        @ParameterizedTest(name = "{index} => {0} = {1}")
        @MethodSource("provideSearchText")
        @DisplayName("정상 url 리턴")
        void getSearchText테스트_정상출력해야됨(String searchText, String expected) {
            DetailedSearchDto dto = DetailedSearchDto.builder().searchText(searchText).build();

            String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, "getSearchText", dto);

            assertThat(url).isEqualTo(expected);
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("getLocation 메서드 테스트")
    class getLocationTest {

        private Stream<Arguments> provideLocation() {
            return Stream.of(
                    Arguments.of(List.of(GANGNAM, GANGDONG), "&local=I010%2CI020"),
                    Arguments.of(List.of(SEOCHO), "&local=I150"),
                    Arguments.of(List.of(BUNDANG, YONGSAN, JONGNO), "&local=B150%2CI210%2CI230")
            );
        }

        @ParameterizedTest(name = "{index} => {0} = {1}")
        @MethodSource("provideLocation")
        @DisplayName("정상 url 리턴")
        void getLocation테스트_정상출력해야됨(List<Location> locations, String expected) {
            DetailedSearchDto dto = DetailedSearchDto.builder().location(locations).build();

            String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, "getLocation", dto);

            assertThat(url).isEqualTo(expected);
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("getJobType 메서드 테스트")
    class getJobTypeTest {

        private Stream<Arguments> provideJob() {
            return Stream.of(
                    Arguments.of(List.of(FULL_TIME, TEMPORARY), "&jobtype=1%2C2"),
                    Arguments.of(List.of(FULL_TIME, FREELANCER), "&jobtype=1%2C6"),
                    Arguments.of(List.of(FULL_TIME, INTERN, MILITARY), "&jobtype=1%2C3%2C9")
            );
        }

        @ParameterizedTest(name = "{index} => {0} = {1}")
        @MethodSource("provideJob")
        @DisplayName("정상 url 리턴")
        void getLocation테스트_정상출력해야됨(List<JobType> jobType, String expected) {
            DetailedSearchDto dto = DetailedSearchDto.builder().jobType(jobType).build();

            String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, "getJobType", dto);

            assertThat(url).isEqualTo(expected);
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("getCareer 메서드 테스트")
    class getCareerTest {

        private Stream<Arguments> provideCareer() {
            return Stream.of(
                    Arguments.of(JUNIOR, "1", "2", "&careerType=1&careerMin=1&careerMax=2"),
                    Arguments.of(SENIOR, "5", "6", "&careerType=2&careerMin=5&careerMax=6"),
                    Arguments.of(SENIOR, null, "10", "&careerType=2&careerMax=10"),
                    Arguments.of(ANY, "3", null, "&careerType=4&careerMin=3"),
                    Arguments.of(ANY, null, null, "&careerType=4")
            );
        }

        @ParameterizedTest(name = "{index} => {0}, {1}, {2} = {3}")
        @MethodSource("provideCareer")
        @DisplayName("정상 url 리턴")
        void getCareer테스트_정상출력해야됨(CareerType careerType, String careerMin, String careerMax, String expected) {
            DetailedSearchDto dto = DetailedSearchDto.builder().career(new DetailedSearchDto.Career(careerType, careerMin, careerMax)).build();

            String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, "getCareer", dto);

            assertThat(url).isEqualTo(expected);
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("getPay 메서드 테스트")
    class getPayTest {

        private Stream<Arguments> providePay() {
            return Stream.of(
                    Arguments.of(ANNUAL, "1000", "2000", "&payType=1&payMin=1000&payMax=2000"),
                    Arguments.of(ANNUAL, "5000", "6000", "&payType=1&payMin=5000&payMax=6000"),
                    Arguments.of(MONTH, null, "3000", "&payType=2&payMax=3000"),
                    Arguments.of(WEEK, "30", null, "&payType=3&payMin=30"),
                    Arguments.of(WEEK, null, "50", "&payType=3&payMax=50"),
                    Arguments.of(MONTH, null, null, "&payType=2")
            );
        }

        @ParameterizedTest(name = "{index} => {0}, {1}, {2} = {3}")
        @MethodSource("providePay")
        @DisplayName("정상 url 리턴")
        void getPay테스트_정상출력해야됨(PayType payType, String payMin, String payMax, String expected) {
            DetailedSearchDto dto = DetailedSearchDto.builder().pay(new DetailedSearchDto.Pay(payType, payMin, payMax)).build();

            String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, "getPay", dto);

            assertThat(url).isEqualTo(expected);
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("빈 문자열 리턴")
    class blankReturnTest {

        private Stream<Arguments> provideMethod() {
            return Stream.of(
                    Arguments.of("getSearchText"),
                    Arguments.of("getLocation"),
                    Arguments.of("getPay"),
                    Arguments.of("getCareer"),
                    Arguments.of("getJobType")
            );
        }

        @ParameterizedTest(name = "{index} => {0}")
        @MethodSource("provideMethod")
        void 빈_문자열_리턴해야됨(String methodName) {
            DetailedSearchDto dto = DetailedSearchDto.builder().build();

            String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, methodName, dto);

            assertThat(url).isEqualTo("");
        }
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