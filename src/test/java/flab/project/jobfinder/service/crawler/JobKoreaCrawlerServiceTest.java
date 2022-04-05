package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.dto.DetailedSearchDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static flab.project.jobfinder.util.Location.*;
import static org.assertj.core.api.Assertions.assertThat;

class JobKoreaCrawlerServiceTest {

    JobKoreaCrawlerService  jobKoreaCrawlerService = new JobKoreaCrawlerService();

    @Test
    void getLocation테스트_정상출력해야됨() {
        DetailedSearchDto dto = DetailedSearchDto.builder().location(List.of(GANGNAM, GANGDONG)).build();

        String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, "getLocation", dto);

        assertThat(url).isEqualTo("&local=I010%2CI020");
    }

    @Test
    void getLocation테스트_빈_문자열_리턴해야됨() {
        DetailedSearchDto dto = DetailedSearchDto.builder().build();

        String url = ReflectionTestUtils.invokeMethod(jobKoreaCrawlerService, "getLocation", dto);

        assertThat(url).isEqualTo("");
    }
}