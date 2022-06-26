package flab.project.jobfinder.controller;

import flab.project.jobfinder.config.UserPropertiesConfig;
import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserPropertiesConfig userPropertiesConfig;

    @GetMapping("/my-page")
    public String myPage(Model model) {

        return "my-page";
    }
    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("signUpFormDto", new SignUpFormDto());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Valid SignUpFormDto signUpFormDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "sign-up";
        }
        userService.save(signUpFormDto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginFormDto", new LoginFormDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginFormDto loginFormDto, HttpServletRequest request) {
        User loginUser = userService.login(loginFormDto);

        HttpSession session = request.getSession();
        session.setAttribute(userPropertiesConfig.getLoginSessionId(), loginUser);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
