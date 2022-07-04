package flab.project.jobfinder.exception.user;

import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.enums.exception.SignUpFailedErrorCode;
import lombok.Getter;

@Getter
public class SignUpFailedException extends RuntimeException {

    private static final String ERROR_MSG = "회원가입 실패: %s";
    private final SignUpFormDto signUpFormDto;
    private final SignUpFailedErrorCode code;

    public SignUpFailedException(SignUpFormDto dto, SignUpFailedErrorCode code) {
        super(ERROR_MSG.formatted(code.errorMsg()));
        this.signUpFormDto = dto;
        this.code = code;
    }
}
