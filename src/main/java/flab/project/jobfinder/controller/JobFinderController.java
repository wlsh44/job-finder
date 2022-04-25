package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.ParseDto;
import flab.project.jobfinder.enums.Location;
import flab.project.jobfinder.service.JobFindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class JobFinderController {

    private final JobFindService jobKoreaJobFindService;

    @GetMapping("/job-find")
    public String getJobFindForm(Model model) {
        DetailedSearchDto dto = new DetailedSearchDto();
        model.addAttribute("dto", dto);
        model.addAttribute("map", Location.toMap());
        return "form";
    }

    @PostMapping("/job-find")
    public String jobFind(Model model, DetailedSearchDto dto) {
        List<ParseDto> list = new ArrayList<>();

        log.info(dto.toString());
        List<ParseDto> jobKoreaList = jobKoreaJobFindService.find(dto);
        list.addAll(jobKoreaList);
        model.addAttribute("list", list);
        for (ParseDto parseDto : jobKoreaList) {
            log.info(parseDto.toString());
        }
        return "recruits";
    }
}
