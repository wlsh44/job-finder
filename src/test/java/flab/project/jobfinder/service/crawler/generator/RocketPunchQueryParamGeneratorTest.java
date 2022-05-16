package flab.project.jobfinder.service.crawler.generator;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.config.RocketPunchPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class RocketPunchQueryParamGeneratorTest {

    RocketPunchQueryParamGenerator rocketPunchQueryParamGenerator;

    RocketPunchPropertiesConfig config;

    @BeforeAll
    void init() {
        config = mock(RocketPunchPropertiesConfig.class);
        rocketPunchQueryParamGenerator = new RocketPunchQueryParamGenerator(config);

        given(config.getDelimiter()).willReturn("&");
    }

    @ParameterizedTest(name = "{index} => {1}")
    @MethodSource("provideDto")
    @DisplayName("정상 url 리턴")
    void 정상_url_리턴(DetailedSearchDto dto, String expected) {
        String result =  rocketPunchQueryParamGenerator.toQueryParams(dto, 1);

        assertThat(result).isEqualTo(expected);
    }

    private Stream<Arguments> provideDto() {
        return Stream.of(
                Arguments.of(DetailedSearchDto.builder().searchText("spring").build(), "keywords=spring&page=1"),
                Arguments.of(DetailedSearchDto.builder().searchText("spring boot")
                                .career(new DetailedSearchDto.Career(JUNIOR, null, 2)).build()
                        , "keywords=spring%20boot&career_type=1&page=1"),
                Arguments.of(DetailedSearchDto.builder().searchText("웹 서비스")
                                .location(List.of(GANGNAM, BUNDANG))
                                .pay(new DetailedSearchDto.Pay(ANNUAL, 4000, null)).build()
                        , "keywords=웹%20서비스&location=강남구&location=분당구&salary=40000000-200000000&page=1"),
                Arguments.of(DetailedSearchDto.builder().searchText("react 웹 프런트")
                                .location(List.of(DONGDAEMUN, EUNPYEONG, YONGSAN))
                                .jobType(List.of(MILITARY)).build()
                        , "keywords=react%20웹%20프런트&location=동대문구&location=은평구&location=용산구&hiring_types=3&page=1")
        );
    }
}