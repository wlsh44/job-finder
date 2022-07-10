package flab.project.jobfinder.controller.advice;

import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.enums.exception.SignUpFailedErrorCode;
import flab.project.jobfinder.exception.user.LoginFailedException;
import flab.project.jobfinder.exception.user.SignUpFailedException;
import flab.project.jobfinder.exception.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

import static flab.project.jobfinder.enums.exception.SignUpFailedErrorCode.ALREADY_EXISTS_USER;
import static flab.project.jobfinder.enums.exception.SignUpFailedErrorCode.PASSWORD_CONFIRM_NOT_CORRECT;

@Slf4j
@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public String UserNotFoundException(UserNotFoundException e, Model model) {
        log.info(e.getMessage());
        return "/";
    }

    @ExceptionHandler(LoginFailedException.class)
    public ModelAndView LoginFailedException(LoginFailedException e) {
        log.info(e.getMessage());

        final BindingResult bindingResult = e.getBindingResult();

        final ModelAndView mav = new ModelAndView("login", bindingResult.getModel());
        mav.addObject("loginFormDto", new LoginFormDto());

        return mav;
    }

    @ExceptionHandler(SignUpFailedException.class)
    public String SignUpFailedException(SignUpFailedException e, Model model) {
        switch (e.getCode()) {
            case ALREADY_EXISTS_USER -> {
                log.info("user email: {}", e.getSignUpFormDto().getEmail());
                SignUpFormDto prevForm = e.getSignUpFormDto();
                SignUpFormDto signUpFormDto = SignUpFormDto.builder()
                        .name(prevForm.getName())
                        .build();
                model.addAttribute("signUpFormDto", signUpFormDto);
                model.addAttribute("emailError", ALREADY_EXISTS_USER.errorMsg());
            }
            case PASSWORD_CONFIRM_NOT_CORRECT -> {
                log.info("password: {}, passwordConfirm: {}", e.getSignUpFormDto().getPassword(), e.getSignUpFormDto().getPasswordConfirm());
                SignUpFormDto prevForm = e.getSignUpFormDto();
                SignUpFormDto signUpFormDto = SignUpFormDto.builder()
                        .email(prevForm.getEmail())
                        .name(prevForm.getName())
                        .build();
                model.addAttribute("signUpFormDto", signUpFormDto);
                model.addAttribute("passwordError", PASSWORD_CONFIRM_NOT_CORRECT.errorMsg());
            }
        }
        return "sign-up";
    }
}
