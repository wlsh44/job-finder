package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitDto;
import flab.project.jobfinder.dto.RecruitPageDto;
import flab.project.jobfinder.enums.Location;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.service.JobFindFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class JobFinderController {

    private final JobFindFactory jobFindFactory;

    @GetMapping("/job-find")
    public String getJobFindForm(Model model) {
        DetailedSearchDto searchFormDto = new DetailedSearchDto();
        Integer currentPage = 1;

        model.addAttribute("searchFormDto", searchFormDto);
        model.addAttribute("locationMap", Location.getDistrictMap());
        model.addAttribute("platformMap", Platform.getKoreaNameMap());
        model.addAttribute("currentPage", currentPage);
        return "form";
    }

    @PostMapping("/job-find")
    public String jobFind(Model model, DetailedSearchDto searchFormDto, @RequestParam Integer currentPage) {
        log.info(searchFormDto.toString());
        RecruitPageDto recruitPageDto = jobFindFactory.getRecruitPageDto(searchFormDto, currentPage);

        List<RecruitDto> recruitDtoList = recruitPageDto.getList();
        int totalPage = recruitPageDto.getTotalPage();
        int startPage = recruitPageDto.getStartPage();

        model.addAttribute("recruitDtoList", recruitDtoList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("searchFormDto", searchFormDto);
        return "recruits";
    }
}
