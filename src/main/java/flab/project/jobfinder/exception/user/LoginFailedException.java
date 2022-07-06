package flab.project.jobfinder.exception.user;

import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.enums.exception.LoginFailedErrorCode;
import lombok.Getter;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
public class LoginFailedException extends MethodArgumentNotValidException {

    public static final String FAILED_MSG = "로그인에 실패했습니다: %s";
//    private final LoginFormDto loginFormDto;
    private final LoginFailedErrorCode code;

    public LoginFailedException(LoginFailedErrorCode code, MethodParameter methodParameter, BindingResult bindingResult) {
        super(methodParameter, bindingResult);
//        this.loginFormDto = dto;
        this.code = code;
    }
}
