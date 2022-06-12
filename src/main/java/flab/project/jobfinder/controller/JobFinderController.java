package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.*;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.enums.location.District;
import flab.project.jobfinder.service.JobFindFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("locationMap", District.getDistrictMap());
        model.addAttribute("platformMap", Platform.getKoreaNameMap());
        return "form";
    }

    @PostMapping("/job-find")
    public String jobFind(@Valid SearchFormDto searchFormDto, BindingResult bindingResult, Model model) {
        log.debug(searchFormDto.toString());

        if (bindingResult.hasErrors()) {
            model.addAttribute("locationMap", District.getDistrictMap());
            model.addAttribute("platformMap", Platform.getKoreaNameMap());
            log.info("errors={}", bindingResult);
            return "form";
        }

        DetailedSearchDto detailedSearchDto = searchFormDto.getDetailedSearchDto();
        Integer currentPage = searchFormDto.getCurrentPage();

        RecruitPageDto recruitPageDto = jobFindFactory.getRecruitPageDto(detailedSearchDto, currentPage);

        List<RecruitDto> recruitDtoList = recruitPageDto.getRecruitDtoList();
        PageDto pageDto = recruitPageDto.getPageDto();
        int totalPage = pageDto.getTotalPage();
        int startPage = pageDto.getStartPage();

        model.addAttribute("recruitDtoList", recruitDtoList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("searchFormDto", searchFormDto);
        return "recruits";
    }
}
