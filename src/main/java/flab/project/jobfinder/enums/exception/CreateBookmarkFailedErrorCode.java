package flab.project.jobfinder.enums.exception;

public enum CreateBookmarkFailedErrorCode implements ErrorCode{
    REQUIRED_AT_LEAST_ONE_CATEGORY("하나 이상의 카테고리가 필요");

    private final String errorMsg;

    CreateBookmarkFailedErrorCode(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String errorMsg() {
        return errorMsg;
    }
}
