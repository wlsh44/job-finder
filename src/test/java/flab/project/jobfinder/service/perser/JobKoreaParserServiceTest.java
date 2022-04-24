package flab.project.jobfinder.service.perser;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.ParseDto;
import flab.project.jobfinder.service.crawler.JobKoreaCrawlerService;
import flab.project.jobfinder.service.crawler.generator.JobKoreaQueryParamGenerator;
import flab.project.jobfinder.service.perser.handler.JobKoreaParseHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static flab.project.jobfinder.enums.JobType.MILITARY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {JobKoreaParserService.class, JobKoreaCrawlerService.class, JobKoreaQueryParamGenerator.class, JobKoreaParseHandler.class})
@EnableConfigurationProperties(value = JobKoreaPropertiesConfig.class)
@TestPropertySource("classpath:application-dev.properties")
class JobKoreaParserServiceTest {

    @Autowired
    JobKoreaParserService parserService;
    
    @Test
    @DisplayName("실제 파싱 테스트")
    void parse_test() {
        DetailedSearchDto dto = DetailedSearchDto.builder().searchText("spring").jobType(List.of(MILITARY)).build();

        List<ParseDto> dtoList = parserService.parse(dto);

        assertThat(dtoList.isEmpty()).isFalse();
    }
}