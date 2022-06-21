package flab.project.jobfinder.service;

import flab.project.jobfinder.config.jobkorea.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.form.DetailedSearchDto;
import flab.project.jobfinder.dto.page.RecruitPageDto;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.service.jobfind.JobKoreaJobFindService;
import flab.project.jobfinder.service.jobfind.crawler.JobKoreaCrawlerService;
import flab.project.jobfinder.service.jobfind.crawler.generator.JobKoreaQueryParamGenerator;
import flab.project.jobfinder.service.jobfind.parser.JobKoreaParserService;
import flab.project.jobfinder.service.jobfind.parser.pagination.JobKoreaPaginationParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {JobKoreaJobFindService.class, JobKoreaCrawlerService.class, JobKoreaQueryParamGenerator.class, JobKoreaParserService.class, JobKoreaPaginationParser.class})
@EnableConfigurationProperties(value = JobKoreaPropertiesConfig.class)
@TestPropertySource("classpath:application-dev.properties")
class JobKoreaJobFindServiceTest {

    @Autowired
    JobKoreaJobFindService parserService;
    
    @Test
    @DisplayName("실제 파싱 테스트")
    void parse_test() {
        DetailedSearchDto dto = DetailedSearchDto.builder().searchText("spring").platform(Platform.JOBKOREA).build();

        RecruitPageDto dtoList = parserService.findJobByPage(dto, 1);

        assertThat(dtoList.getRecruitDtoList().isEmpty()).isFalse();
    }
}