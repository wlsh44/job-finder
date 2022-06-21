package flab.project.jobfinder.service.crawler.generator;

import flab.project.jobfinder.dto.form.DetailedSearchDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static flab.project.jobfinder.enums.CareerType.ANY;
import static flab.project.jobfinder.enums.CareerType.JUNIOR;
import static flab.project.jobfinder.enums.JobType.MILITARY;
import static flab.project.jobfinder.enums.Location.*;
import static flab.project.jobfinder.enums.PayType.ANNUAL;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RocketPunchQueryParamGeneratorTest {

    RocketPunchQueryParamGenerator rocketPunchQueryParamGenerator;

    @BeforeEach
    void init() {
        rocketPunchQueryParamGenerator = new RocketPunchQueryParamGenerator();
    }

    @ParameterizedTest(name = "{index} => {1}")
    @MethodSource("provideDto")
    @DisplayName("정상 url 리턴")
    void 정상_url_리턴(DetailedSearchDto dto, MultiValueMap<String, String> expected) {
        MultiValueMap<String, String> result =  rocketPunchQueryParamGenerator.toQueryParams(dto, 1);

        assertThat(result).isEqualTo(expected);
    }

    private Stream<Arguments> provideDto() {
        MultiValueMap<String, String> map1 = new LinkedMultiValueMap<>(Map.of(
                "page", List.of("1")));
        MultiValueMap<String, String> map2 = new LinkedMultiValueMap<>(Map.of(
                "keywords", List.of("spring"),
                "page", List.of("1")));
        MultiValueMap<String, String> map3 = new LinkedMultiValueMap<>(Map.of(
                "keywords", List.of("spring boot"),
                "career_type", List.of("1"),
                "page", List.of("1")));
        MultiValueMap<String, String> map4 = new LinkedMultiValueMap<>(Map.of(
                "keywords", List.of("react 웹 프런트"),
                "location", List.of("동대문구", "은평구", "용산구"),
                "hiring_types", List.of("3"),
                "page", List.of("1")));
        MultiValueMap<String, String> map5 = new LinkedMultiValueMap<>(Map.of(
                "keywords", List.of("웹 서비스"),
                "location", List.of("강남구", "분당구"),
                "salary", List.of("40000000-"),
                "page", List.of("1")));
        MultiValueMap<String, String> map6 = new LinkedMultiValueMap<>(Map.of(
                "keywords", List.of("테스트"),
                "page", List.of("1")));

        return Stream.of(
                Arguments.of(DetailedSearchDto.builder().build(), map1),
                Arguments.of(DetailedSearchDto.builder().searchText("spring").build(), map2),
                Arguments.of(DetailedSearchDto.builder().searchText("spring boot")
                                .career(new DetailedSearchDto.Career(JUNIOR, null, 2)).build()
                        , map3),
                Arguments.of(DetailedSearchDto.builder().searchText("react 웹 프런트")
                                .location(List.of(DONGDAEMUN, EUNPYEONG, YONGSAN))
                                .jobType(List.of(MILITARY)).build()
                        , map4),
                Arguments.of(DetailedSearchDto.builder().searchText("웹 서비스")
                                .location(List.of(GANGNAM, BUNDANG))
                                .pay(new DetailedSearchDto.Pay(ANNUAL, 4000, null)).build()
                        , map5),
                Arguments.of(DetailedSearchDto.builder().searchText("테스트")
                                .career(new DetailedSearchDto.Career(ANY, 1, null)).build()
                        , map6)
        );
    }
}