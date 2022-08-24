package flab.project.jobfinder.service;

import flab.project.jobfinder.config.jobkorea.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.form.DetailedSearchDto;
import flab.project.jobfinder.dto.page.RecruitPageDto;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.service.jobfind.JobKoreaJobFindService;
import flab.project.jobfinder.service.jobfind.crawler.JobKoreaCrawlerService;
import flab.project.jobfinder.service.jobfind.crawler.generator.JobKoreaQueryParamGenerator;
import flab.project.jobfinder.service.jobfind.parser.JobKoreaParserService;
import flab.project.jobfinder.service.jobfind.parser.duedate.DueDateParser;
import flab.project.jobfinder.service.jobfind.parser.pagination.JobKoreaPaginationParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        JobKoreaJobFindService.class,
        JobKoreaCrawlerService.class,
        JobKoreaQueryParamGenerator.class,
        JobKoreaParserService.class,
        JobKoreaPaginationParser.class,
        DueDateParser.class
})
@EnableConfigurationProperties(value = JobKoreaPropertiesConfig.class)
class JobKoreaJobFindServiceTest {

    @Autowired
    JobKoreaJobFindService parserService;

    DetailedSearchDto dto;

    @BeforeEach
    void init() {
        dto = DetailedSearchDto.builder()
                .searchText("spring")
                .platform(Platform.JOBKOREA)
                .build();
    }

    @Test
    @DisplayName("실제 파싱 테스트")
    void parse_test() {
        //given

        //when
        RecruitPageDto dtoList = parserService.findJobByPage(dto, 1);

        //then
        assertThat(dtoList.getRecruitDtoList().isEmpty()).isFalse();
    }
}