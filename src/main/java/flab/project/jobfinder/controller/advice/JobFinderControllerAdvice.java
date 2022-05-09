package flab.project.jobfinder.controller.advice;

import flab.project.jobfinder.dto.SearchFormDto;
import flab.project.jobfinder.enums.Location;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.exception.CrawlFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class JobFinderControllerAdvice {

    @ExceptionHandler(CrawlFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String CrawlFailedException(Model model) {
        SearchFormDto searchFormDto = new SearchFormDto();

        model.addAttribute("searchFormDto", searchFormDto);
        model.addAttribute("locationMap", Location.getDistrictMap());
        model.addAttribute("platformMap", Platform.getKoreaNameMap());
        return "form";
    }
}
