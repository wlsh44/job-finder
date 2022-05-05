package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitDto;
import flab.project.jobfinder.dto.RecruitPageDto;
import flab.project.jobfinder.enums.Location;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.service.JobFindService;
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

    private final JobFindService jobKoreaJobFindService;

    @GetMapping("/job-find")
    public String getJobFindForm(Model model) {
        DetailedSearchDto dto = new DetailedSearchDto();
        Integer currentPage = 1;

        model.addAttribute("dto", dto);
        model.addAttribute("locationMap", Location.toMap());
        model.addAttribute("platformMap", Platform.toMap());
        model.addAttribute("currentPage", currentPage);
        return "form";
    }

    @PostMapping("/job-find")
    public String jobFind(Model model, DetailedSearchDto dto, @RequestParam Integer currentPage) {
        /* TODO
         * 검증 로직
         * pagination
         */
        log.info(dto.toString());
        RecruitPageDto recruitPageDto = getRecruitPageDto(dto, currentPage);

        List<RecruitDto> list = recruitPageDto.getList();
        Integer totalPage = recruitPageDto.getTotalPage();

        model.addAttribute("list", list);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("dto", dto);
//        for (ParseDto parseDto : jobKoreaList) {
//            log.info(parseDto.toString());
//        }
        return "recruits";
    }

    private RecruitPageDto getRecruitPageDto(DetailedSearchDto dto, Integer currentPage) {
        switch (dto.getPlatform()) {
            case JOBKOREA:
                return jobKoreaJobFindService.findJobByPage(dto, currentPage);
        }
        return new RecruitPageDto();
    }
}
