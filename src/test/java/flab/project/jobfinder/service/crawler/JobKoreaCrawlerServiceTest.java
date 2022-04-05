package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.util.CareerType;
import flab.project.jobfinder.util.JobType;
import flab.project.jobfinder.util.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Stream;

import static flab.project.jobfinder.util.CareerType.*;
import static flab.project.jobfinder.util.JobType.*;
import static flab.project.jobfinder.util.Location.*;
import static org.assertj.core.api.Assertions.assertThat;

class JobKoreaCrawlerServiceTest {

    JobKoreaCrawlerService jobKoreaCrawlerService = new JobKoreaCrawlerService();

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("getLocation 메소드 테스트")
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

        @Test
        @DisplayName("빈 문자열 리턴")
        void getLocation테스트_빈_문자열_리턴해야됨() {
            DetailedSearchDto dto = DetailedSearchDto.builder().build();

            String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, "getLocation", dto);

            assertThat(url).isEqualTo("");
        }
    }


    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("getCareer 메소드 테스트")
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

        @Test
        @DisplayName("빈 문자열 리턴")
        void getLocation테스트_빈_문자열_리턴해야됨() {
            DetailedSearchDto dto = DetailedSearchDto.builder().build();

            String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, "getCareer", dto);

            assertThat(url).isEqualTo("");
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("getJobType 메소드 테스트")
    class getJobTypeTest {

        private Stream<Arguments> provideJob() {
            return Stream.of(
                    Arguments.of(List.of(FULL_TIME, PART_TIME), "&jobtype=1%2C2"),
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

        @Test
        @DisplayName("빈 문자열 리턴")
        void getLocation테스트_빈_문자열_리턴해야됨() {
            DetailedSearchDto dto = DetailedSearchDto.builder().build();

            String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, "getJobType", dto);

            assertThat(url).isEqualTo("");
        }
    }

}