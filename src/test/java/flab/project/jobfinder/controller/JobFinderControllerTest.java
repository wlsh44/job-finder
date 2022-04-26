package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.ParseDto;
import flab.project.jobfinder.enums.Location;
import flab.project.jobfinder.service.JobKoreaJobFindService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobFinderController.class)
@ActiveProfiles("dev")
class JobFinderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JobFinderController jobFinderController;

    @MockBean
    JobKoreaJobFindService jobFindService;

    DetailedSearchDto detailedSearchDto = DetailedSearchDto.builder()
                                .searchText("spring")
                                .location(List.of(Location.SEOUL)).build();

    ParseDto parseDto = ParseDto.builder().title("test title")
                            .jobType("test jobType")
                            .dueDate("test dueDate")
                            .corp("test corp")
                            .career("test career")
                            .location("test loc")
                            .platform("JobKorea")
                            .techStack("spring")
                            .build();

    @Test
    void basic_test() throws Exception {
        when(jobFindService.find(detailedSearchDto))
                .thenReturn(List.of(parseDto));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/job-find")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}