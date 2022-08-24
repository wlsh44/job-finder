package flab.project.jobfinder.enums.exception;

public enum SignUpFailedErrorCode implements ErrorCode {
    ALREADY_EXISTS_USER("이미 존재하는 유저입니다."),
    PASSWORD_CONFIRM_NOT_CORRECT("비밀번호가 일치하지 않습니다.");

    private final String errorMsg;

    SignUpFailedErrorCode(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String errorMsg() {
        return errorMsg;
    }
}
