package flab.project.jobfinder.controller.advice;

import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.exception.member.LoginFailedException;
import flab.project.jobfinder.exception.member.SignUpFailedException;
import flab.project.jobfinder.exception.member.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class MemberControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public String UserNotFoundException(UserNotFoundException e, Model model) {
        return "/";
    }

    @ExceptionHandler(LoginFailedException.class)
    public String LoginFailedException(LoginFailedException e, Model model) {
        model.addAttribute("loginFormDto", new LoginFormDto());
        return "redirect:/login";
    }

    @ExceptionHandler(SignUpFailedException.class)
    public String SignUpFailedException(SignUpFailedException e, Model model) {
        model.addAttribute("signUpFormDto", new SignUpFormDto());
        return "redirect:/sign-up";
    }
}
