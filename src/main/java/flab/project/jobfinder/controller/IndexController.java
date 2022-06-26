package flab.project.jobfinder.controller;

import flab.project.jobfinder.config.UserPropertiesConfig;
import flab.project.jobfinder.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserPropertiesConfig config;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(config.getLoginSessionId());
        if (user != null) {
            log.info("email: {}, name: {}", user.getEmail(), user.getName());
            model.addAttribute("user", user);
        }
        return "index";
    }
}
