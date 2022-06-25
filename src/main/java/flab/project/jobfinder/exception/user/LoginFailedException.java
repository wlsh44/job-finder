package flab.project.jobfinder.exception.user;

import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.enums.exception.LoginFailedErrorCode;
import lombok.Getter;

@Getter
public class LoginFailedException extends RuntimeException {

    private final LoginFormDto loginFormDto;
    private final LoginFailedErrorCode code;

    public LoginFailedException(LoginFormDto dto, LoginFailedErrorCode code) {
        super("로그인에 실패했습니다: " + code.errorMsg());
        this.loginFormDto = dto;
        this.code = code;
    }
}
