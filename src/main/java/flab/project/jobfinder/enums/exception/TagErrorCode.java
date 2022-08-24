package flab.project.jobfinder.enums.exception;

public enum TagErrorCode implements ErrorCode{
    ALREADY_EXISTS_TAG("이미 존재하는 태그입니다"),
    TAG_NOT_FOUND("해당 태그가 존재하지 않습니다");

    private final String errorMsg;

    TagErrorCode(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String errorMsg() {
        return errorMsg;
    }
}
