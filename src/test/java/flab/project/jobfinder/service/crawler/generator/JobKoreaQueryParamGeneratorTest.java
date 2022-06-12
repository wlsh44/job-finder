package flab.project.jobfinder.service.crawler.generator;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.enums.location.District;
import flab.project.jobfinder.enums.location.JobKoreaLocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static flab.project.jobfinder.enums.CareerType.JUNIOR;
import static flab.project.jobfinder.enums.JobType.MILITARY;
import static flab.project.jobfinder.enums.PayType.ANNUAL;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class JobKoreaQueryParamGeneratorTest {

    @InjectMocks
    JobKoreaQueryParamGenerator jobKoreaQueryParamGenerator;

    @ParameterizedTest(name = "{index} => {1}")
    @MethodSource("provideDto")
    @DisplayName("정상 QueryParameter 리턴")
    void 정상_url_리턴_Delimiter_사용(DetailedSearchDto dto, MultiValueMap<String, String> expected) {
        MultiValueMap<String, String> result =  jobKoreaQueryParamGenerator.toQueryParams(dto, 1);

        assertThat(result).isEqualTo(expected);
    }

    private Stream<Arguments> provideDto() {
        MultiValueMap<String, String> map1 = new LinkedMultiValueMap<>(Map.of(
                "tabType", List.of("recruit"),
                "Page_No", List.of("1")));
        MultiValueMap<String, String> map2 = new LinkedMultiValueMap<>(Map.of(
                "tabType", List.of("recruit"),
                "stext", List.of("spring"),
                "Page_No", List.of("1")));
        MultiValueMap<String, String> map3 = new LinkedMultiValueMap<>(Map.of(
                "tabType", List.of("recruit"),
                "stext", List.of("spring boot"),
                "careerType", List.of("1"),
                "careerMax", List.of("2"),
                "Page_No", List.of("1")));
        MultiValueMap<String, String> map4 = new LinkedMultiValueMap<>(Map.of(
                "tabType", List.of("recruit"),
                "stext", List.of("react 웹 프런트"),
                "local", List.of("I110", "I220", "I210"),
                "jobtype", List.of("9"),
                "Page_No", List.of("1")));
        MultiValueMap<String, String> map5 = new LinkedMultiValueMap<>(Map.of(
                "tabType", List.of("recruit"),
                "stext", List.of("웹 서비스"),
                "local", List.of("I010", "B150"),
                "payType", List.of("1"),
                "payMin", List.of("4000"),
                "Page_No", List.of("1")));

        return Stream.of(
                Arguments.of(DetailedSearchDto.builder().build(), map1),
                Arguments.of(DetailedSearchDto.builder().searchText("spring").build(), map2),
                Arguments.of(DetailedSearchDto.builder().searchText("spring boot")
                                .career(new DetailedSearchDto.Career(JUNIOR, null, 2)).build()
                        , map3),
                Arguments.of(DetailedSearchDto.builder().searchText("react 웹 프런트")
                                .location(List.of(District.DONGDAEMUN, District.EUNPYEONG, District.YONGSAN))
                                .jobType(List.of(MILITARY)).build()
                        , map4),
                Arguments.of(DetailedSearchDto.builder().searchText("웹 서비스")
                                .location(List.of(District.GANGNAM, District.BUNDANG))
                                .pay(new DetailedSearchDto.Pay(ANNUAL, 4000, null)).build()
                        , map5)
        );
    }
}