package flab.project.jobfinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JobFinderController {

    @GetMapping("/job-find")
    public String jobFind() {
        return "basic/recruits";
    }

}
