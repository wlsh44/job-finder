package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitDto;
import flab.project.jobfinder.dto.RecruitPageDto;
import flab.project.jobfinder.dto.SearchFormDto;
import flab.project.jobfinder.enums.Location;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.service.JobFindFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class JobFinderController {

    private final JobFindFactory jobFindFactory;

    @GetMapping("/job-find")
    public String getJobFindForm(Model model) {
        SearchFormDto searchFormDto = new SearchFormDto();

        model.addAttribute("searchFormDto", searchFormDto);
        model.addAttribute("locationMap", Location.getDistrictMap());
        model.addAttribute("platformMap", Platform.getKoreaNameMap());
        return "form";
    }

    @PostMapping("/job-find")
    public String jobFind(@Valid SearchFormDto searchFormDto, Model model) {
        DetailedSearchDto detailedSearchDto = searchFormDto.getDetailedSearchDto();
        Integer currentPage = searchFormDto.getCurrentPage();

        log.info(searchFormDto.toString());
        RecruitPageDto recruitPageDto = jobFindFactory.getRecruitPageDto(detailedSearchDto, currentPage);

        List<RecruitDto> recruitDtoList = recruitPageDto.getRecruitDtoList();
        int totalPage = recruitPageDto.getTotalPage();
        int startPage = recruitPageDto.getStartPage();

        SearchFormDto newSearchFormDto = new SearchFormDto(detailedSearchDto, currentPage);

        model.addAttribute("recruitDtoList", recruitDtoList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("searchFormDto", newSearchFormDto);
        return "recruits";
    }
}
