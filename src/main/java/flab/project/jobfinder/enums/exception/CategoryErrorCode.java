package flab.project.jobfinder.enums.exception;

public enum CategoryErrorCode implements ErrorCode{
    ALREADY_EXISTS_CATEGORY("이미 존재하는 카테고리");

    private final String errorMsg;

    CategoryErrorCode(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String errorMsg() {
        return errorMsg;
    }
}
