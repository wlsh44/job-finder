package flab.project.jobfinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitDto;
import flab.project.jobfinder.dto.RecruitPageDto;
import flab.project.jobfinder.enums.CareerType;
import flab.project.jobfinder.enums.Location;
import flab.project.jobfinder.enums.PayType;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.service.JobKoreaJobFindService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobFinderController.class)
@ActiveProfiles("dev")
class JobFinderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JobKoreaJobFindService jobFindService;

    DetailedSearchDto detailedSearchDto = DetailedSearchDto.builder()
                                .searchText("spring")
                                .pay(new DetailedSearchDto.Pay(PayType.ANNUAL, null, null))
                                .career(new DetailedSearchDto.Career(CareerType.SENIOR, null, null))
                                .location(List.of(Location.SEOUL))
                                .platform(Platform.JOBKOREA).build();


    RecruitDto recruitDto = RecruitDto.builder()
                                    .title("test title")
                                    .jobType("test jobType")
                                    .dueDate("test dueDate")
                                    .corp("test corp")
                                    .career("test career")
                                    .location("test loc")
                                    .platform("JobKorea")
                                    .techStack("spring")
                                    .build();

    RecruitPageDto recruitPageDto = RecruitPageDto.builder()
                                            .list(List.of(recruitDto))
                                            .totalPage(1)
                                            .startPage(1)
                                            .build();

    @Test
    @DisplayName("get 테스트")
    void getTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/job-find"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("post 테스트")
    void postTest() throws Exception {
        given(jobFindService.findJobByPage(detailedSearchDto, 1))
                .willReturn(recruitPageDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/job-find")
                .param("searchText", "spring")
                .param("location", "SEOUL")
                .param("platform", "JOBKOREA")
                .param("pay.payType", "ANNUAL")
                .param("career.careerType", "SENIOR")
                .param("currentPage", "1")
                .contentType(MediaType.TEXT_HTML)
                .accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk());
    }
}