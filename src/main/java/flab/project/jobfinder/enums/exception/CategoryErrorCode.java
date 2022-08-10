package flab.project.jobfinder.enums.exception;

public enum CategoryErrorCode implements ErrorCode{
    ALREADY_EXISTS_CATEGORY("이미 존재하는 카테고리입니다"),
    CATEGORY_ID_NOT_FOUND("해당 카테고리가 존재하지 않습니다"),
    CATEGORY_NAME_NOT_FOUND("해당 카테고리가 존재하지 않습니다"),
    BOOKMARK_HAS_TAG("북마크에 태그가 존재합니다."),
    TOO_MANY_CATEGORIES("카테고리가 너무 많습니다.");

    private final String errorMsg;

    CategoryErrorCode(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String errorMsg() {
        return errorMsg;
    }
}
