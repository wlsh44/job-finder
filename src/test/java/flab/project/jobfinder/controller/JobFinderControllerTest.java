package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitDto;
import flab.project.jobfinder.dto.RecruitPageDto;
import flab.project.jobfinder.dto.SearchFormDto;
import flab.project.jobfinder.enums.CareerType;
import flab.project.jobfinder.enums.Location;
import flab.project.jobfinder.enums.PayType;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.service.JobFindFactory;
import flab.project.jobfinder.service.JobKoreaJobFindService;
import org.junit.jupiter.api.BeforeEach;
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
    JobFindFactory findFactory;

    SearchFormDto searchFormDto;
    RecruitDto recruitDto;
    RecruitPageDto recruitPageDto;

    @BeforeEach
    void init() {
        searchFormDto = new SearchFormDto();
        searchFormDto.setPlatform(Platform.JOBKOREA);
        searchFormDto.setSearchText("spring");
        searchFormDto.setPayType(PayType.ANNUAL);
        searchFormDto.setCareerType(CareerType.SENIOR);
        searchFormDto.setLocation(List.of(Location.SEOUL));
        searchFormDto.setCurrentPage(1);

        recruitDto = RecruitDto.builder()
                .title("test title")
                .jobType("test jobType")
                .dueDate("test dueDate")
                .corp("test corp")
                .career("test career")
                .location("test loc")
                .platform("JobKorea")
                .techStack("spring")
                .build();

        recruitPageDto = RecruitPageDto.builder()
                .recruitDtoList(List.of(recruitDto))
                .totalPage(1)
                .startPage(1)
                .build();
    }

    @Test
    @DisplayName("get 테스트")
    void getTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/job-find"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("post 성공 테스트")
    void postSuccessTest() throws Exception {
        given(findFactory.getRecruitPageDto(searchFormDto.getDetailedSearchDto(),
                                            searchFormDto.getCurrentPage()))
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

    @Test
    @DisplayName("post 실패 - 플랫폼 선택 안 함")
    void postFail_PlatformIsNull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/job-find")
                        .param("searchText", "spring")
                        .param("location", "SEOUL")
                        .param("pay.payType", "ANNUAL")
                        .param("career.careerType", "SENIOR")
                        .param("currentPage", "1")
                        .contentType(MediaType.TEXT_HTML)
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());    //200이 리턴되는게 맞나?
    }

    @Test
    @DisplayName("post 실패 - currentPage 이상한 값 들어옴")
    void postFail_WrongFormatOfCurrentPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/job-find")
                        .param("platform", "JOBKOREA")
                        .param("currentPage", "-1")
                        .contentType(MediaType.TEXT_HTML)
                        .accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk());    //200이 리턴되는게 맞나?
    }
}