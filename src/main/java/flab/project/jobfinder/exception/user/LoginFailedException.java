package flab.project.jobfinder.exception.user;

import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.enums.exception.LoginFailedErrorCode;
import lombok.Getter;

@Getter
public class LoginFailedException extends RuntimeException {

    public static final String FAILED_MSG = "로그인에 실패했습니다: %s";
    private final LoginFormDto loginFormDto;
    private final LoginFailedErrorCode code;

    public LoginFailedException(LoginFormDto dto, LoginFailedErrorCode code) {
        super(FAILED_MSG.formatted(code.errorMsg()));
        this.loginFormDto = dto;
        this.code = code;
    }
}
