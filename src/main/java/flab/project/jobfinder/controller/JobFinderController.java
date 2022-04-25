package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.ParseDto;
import flab.project.jobfinder.service.perser.ParserService;
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

    private final ParserService jobKoreaParserService;

    @GetMapping("/job-find")
    public String getJobFindForm() {
        return "recruits";
    }

    @PostMapping("/job-find")
    public String jobFind(Model model, @RequestBody DetailedSearchDto dto) {
        List<ParseDto> list = new ArrayList<>();

        log.info(dto.toString());
        List<ParseDto> jobKoreaList = jobKoreaParserService.parse(dto);
        list.addAll(jobKoreaList);
        model.addAttribute("list", list);
        for (ParseDto parseDto : jobKoreaList) {
            log.info(parseDto.toString());
        }
        return "recruits";
    }
}
