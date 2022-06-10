package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.PageDto;
import flab.project.jobfinder.dto.RecruitDto;
import flab.project.jobfinder.dto.RecruitPageDto;
import flab.project.jobfinder.dto.SearchFormDto;
import flab.project.jobfinder.enums.CareerType;
import flab.project.jobfinder.enums.Location;
import flab.project.jobfinder.enums.PayType;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.service.JobFindFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobFinderController.class)
class RocketPunchJobFinderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JobFindFactory findFactory;

    SearchFormDto searchFormDto;
    RecruitDto recruitDto;
    RecruitPageDto recruitPageDto;
    PageDto pageDto;

    @BeforeEach
    void init() {
        searchFormDto = new SearchFormDto();
        searchFormDto.setPlatform(Platform.ROCKETPUNCH);
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
                .platform("로켓펀치")
                .techStack("spring")
                .build();

        pageDto = PageDto.builder()
                .startPage(1)
                .totalPage(1)
                .build();

        recruitPageDto = RecruitPageDto.builder()
                .recruitDtoList(List.of(recruitDto))
                .pageDto(pageDto)
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
                        .param("platform", "ROCKETPUNCH")
                        .param("payType", "ANNUAL")
                        .param("careerType", "SENIOR")
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
    @DisplayName("post 실패 - currentPage 값 안 들어옴")
    void postFail_CurrentPageNull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/job-find")
                        .param("platform", "ROCKETPUNCH")
                        .contentType(MediaType.TEXT_HTML)
                        .accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("post 실패 - currentPage 이상한 값 들어옴")
    void postFail_WrongFormatOfCurrentPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/job-find")
                        .param("platform", "ROCKETPUNCH")
                        .param("currentPage", "-1")
                        .contentType(MediaType.TEXT_HTML)
                        .accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("post 실패 - careerMin 이상한 값 들어옴")
    void postFail_WrongFormatOfCareerMin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/job-find")
                        .param("platform", "ROCKETPUNCH")
                        .param("currentPage", "1")
                        .param("careerMin", "aaa")
                        .contentType(MediaType.TEXT_HTML)
                        .accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("post 실패 - careerMax 이상한 값 들어옴")
    void postFail_WrongFormatOfCareerMax() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/job-find")
                        .param("platform", "ROCKETPUNCH")
                        .param("currentPage", "1")
                        .param("careerMax", "aaa")
                        .contentType(MediaType.TEXT_HTML)
                        .accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("post 실패 - payMin 이상한 값 들어옴")
    void postFail_WrongFormatOfPayMin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/job-find")
                        .param("platform", "ROCKETPUNCH")
                        .param("currentPage", "1")
                        .param("payMin", "aaa")
                        .contentType(MediaType.TEXT_HTML)
                        .accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("post 실패 - payMax 이상한 값 들어옴")
    void postFail_WrongFormatOfPayMax() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/job-find")
                        .param("platform", "ROCKETPUNCH")
                        .param("currentPage", "1")
                        .param("payMax", "aaa")
                        .contentType(MediaType.TEXT_HTML)
                        .accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk());
    }
}