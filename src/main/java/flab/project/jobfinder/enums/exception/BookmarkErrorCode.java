package flab.project.jobfinder.enums.exception;

public enum BookmarkErrorCode implements ErrorCode{
    REQUIRED_AT_LEAST_ONE_CATEGORY("하나 이상의 카테고리가 필요"),
    BOOKMARK_NAME_NOT_FOUND("해당 북마크가 존재하지 않습니다. 북마크 이름: %s"),
    BOOKMARK_ID_NOT_FOUND("해당 북마크가 존재하지 않습니다. 북마크 id: %d");

    private final String errorMsg;

    BookmarkErrorCode(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String errorMsg() {
        return errorMsg;
    }
}
