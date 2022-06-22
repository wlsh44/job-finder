package flab.project.jobfinder.controller;

import flab.project.jobfinder.config.MemberPropertiesConfig;
import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.dto.member.Member;
import flab.project.jobfinder.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberPropertiesConfig memberPropertiesConfig;

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("signUpFormDto", new SignUpFormDto());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Valid SignUpFormDto signUpFormDto) {
        memberService.save(signUpFormDto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginFormDto", new LoginFormDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginFormDto loginFormDto, HttpServletRequest request) {
        Member loginMember = memberService.login(loginFormDto);

        HttpSession session = request.getSession();
        session.setAttribute(memberPropertiesConfig.getLoginSessionId(), loginMember);
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
