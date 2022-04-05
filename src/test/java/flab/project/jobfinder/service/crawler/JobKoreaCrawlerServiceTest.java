package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.dto.DetailedSearchDto;
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
        @DisplayName("정상 url 출력")
        void getLocation테스트_정상출력해야됨(List<Location> locations, String expected) {
            DetailedSearchDto dto = DetailedSearchDto.builder().location(locations).build();

            String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, "getLocation", dto);

            assertThat(url).isEqualTo(expected);
        }

        @Test
        @DisplayName("빈 문차열 출력")
        void getLocation테스트_빈_문자열_리턴해야됨() {
            DetailedSearchDto dto = DetailedSearchDto.builder().build();

            String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, "getLocation", dto);

            assertThat(url).isEqualTo("");
        }
    }


}