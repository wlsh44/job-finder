package flab.project.jobfinder.service;

import flab.project.jobfinder.config.rocketpunch.RocketPunchConfig;
import flab.project.jobfinder.config.rocketpunch.RocketPunchPropertiesConfig;
import flab.project.jobfinder.dto.form.DetailedSearchDto;
import flab.project.jobfinder.dto.page.RecruitPageDto;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.service.jobfind.RocketPunchJobFindService;
import flab.project.jobfinder.service.jobfind.crawler.RocketPunchCrawlerService;
import flab.project.jobfinder.service.jobfind.parser.RocketPunchParserService;
import flab.project.jobfinder.service.jobfind.parser.duedate.DueDateParser;
import flab.project.jobfinder.service.jobfind.parser.pagination.RocketPunchPaginationParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        RocketPunchJobFindService.class,
        RocketPunchCrawlerService.class,
        RocketPunchParserService.class,
        RocketPunchConfig.class,
        RocketPunchPaginationParser.class,
        DueDateParser.class
})
@EnableConfigurationProperties(value = RocketPunchPropertiesConfig.class)
class RocketPunchJobFindServiceTest {

    @Autowired
    RocketPunchJobFindService parserService;

    DetailedSearchDto dto;

    @BeforeEach
    void init() {
         dto = DetailedSearchDto.builder()
                .searchText("spring")
                .platform(Platform.ROCKETPUNCH)
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