package flab.project.jobfinder.service;

import flab.project.jobfinder.config.RocketPunchConfig;
import flab.project.jobfinder.config.RocketPunchPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitPageDto;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.service.crawler.RocketPunchCrawlerService;
import flab.project.jobfinder.service.parser.RocketPunchParserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {RocketPunchJobFindService.class, RocketPunchCrawlerService.class, RocketPunchParserService.class, RocketPunchConfig.class})
@EnableConfigurationProperties(value = RocketPunchPropertiesConfig.class)
class RocketPunchJobFindServiceTest {

    @Autowired
    RocketPunchJobFindService parserService;

    @Test
    @DisplayName("실제 파싱 테스트")
    void parse_test() {
        DetailedSearchDto dto = DetailedSearchDto.builder()
                .searchText("spring")
                .platform(Platform.ROCKETPUNCH)
                .build();

        RecruitPageDto dtoList = parserService.findJobByPage(dto, 1);

        assertThat(dtoList.getRecruitDtoList().isEmpty()).isFalse();
    }
}