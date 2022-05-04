package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitDto;
import flab.project.jobfinder.dto.RecruitPageDto;
import flab.project.jobfinder.enums.Location;
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

import static org.mockito.Mockito.when;
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
                                .location(List.of(Location.SEOUL)).build();

    RecruitDto recruitDto = RecruitDto.builder().title("test title")
                            .jobType("test jobType")
                            .dueDate("test dueDate")
                            .corp("test corp")
                            .career("test career")
                            .location("test loc")
                            .platform("JobKorea")
                            .techStack("spring")
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
        when(jobFindService.findJobByPage(detailedSearchDto, 1))
                .thenReturn(RecruitPageDto
                        .builder()
                        .list(List.of(recruitDto))
                        .totalPage(1)
                        .build());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/job-find")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}