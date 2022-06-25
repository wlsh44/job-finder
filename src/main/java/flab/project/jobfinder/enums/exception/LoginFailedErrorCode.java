package flab.project.jobfinder.enums.exception;

public enum LoginFailedErrorCode {

    NOT_EXISTS_USER("존재하지 않는 유저"),
    WRONG_PASSWORD("비밀번호 틀림");

    private final String errorMsg;

    LoginFailedErrorCode(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String errorMsg() {
        return errorMsg;
    }
}
