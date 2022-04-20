package flab.project.jobfinder.service.crawler.generator;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static flab.project.jobfinder.enums.CareerType.JUNIOR;
import static flab.project.jobfinder.enums.JobType.MILITARY;
import static flab.project.jobfinder.enums.Location.*;
import static flab.project.jobfinder.enums.PayType.ANNUAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class JobKoreaQueryParamGeneratorTest {

    @InjectMocks
    JobKoreaQueryParamGenerator jobKoreaQueryParamGenerator;

    @Mock
    JobKoreaPropertiesConfig config;

    @ParameterizedTest(name = "{index} => {1}")
    @MethodSource("provideDto")
    @DisplayName("정상 url 리턴")
    void 정상_url_리턴(DetailedSearchDto dto, String expected) {
        String result =  jobKoreaQueryParamGenerator.toQueryParams(dto);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{index} => {1}")
    @MethodSource("provideDtoUsingDelimiter")
    @DisplayName("정상 url 리턴 구분자 사용")
    void 정상_url_리턴_Delimiter_사용(DetailedSearchDto dto, String expected) {
        when(config.getDelimiter()).thenReturn("%2C");

        String result =  jobKoreaQueryParamGenerator.toQueryParams(dto);

        assertThat(result).isEqualTo(expected);
    }

    private Stream<Arguments> provideDtoUsingDelimiter() {
        return Stream.of(
                Arguments.of(DetailedSearchDto.builder().searchText("웹 서비스")
                        .location(List.of(GANGNAM, BUNDANG))
                        .pay(new DetailedSearchDto.Pay(ANNUAL, "4000", null)).build()
                        , "stext=%EC%9B%B9+%EC%84%9C%EB%B9%84%EC%8A%A4&local=I010%2CB150&payType=1&payMin=4000")

        );
    }

    private Stream<Arguments> provideDto() {
        return Stream.of(
                Arguments.of(DetailedSearchDto.builder().build(), ""),
                Arguments.of(DetailedSearchDto.builder().searchText("spring").build(), "stext=spring"),
                Arguments.of(DetailedSearchDto.builder().searchText("spring boot")
                                .career(new DetailedSearchDto.Career(JUNIOR, null, "2")).build()
                        , "stext=spring+boot&careerType=1&careerMax=2"),
                Arguments.of(DetailedSearchDto.builder().searchText("react 웹 프런트")
                                .location(List.of(DONGDAEMUN, EUNPYEONG, YONGSAN))
                                .jobType(List.of(MILITARY)).build()
                        , "stext=react+%EC%9B%B9+%ED%94%84%EB%9F%B0%ED%8A%B8&local=I110%2CI220%2CI210&jobtype=9")
        );
    }
}