package flab.project.jobfinder.enums.bookmark;

public enum CategoryResponseCode {
    CREATE_CATEGORY("카테고리 생성"),
    FAILED_CREATE_CATEGORY("카테고리 생성 실패"),
    DELETE_CATEGORY("카테고리 삭제"),
    FAILED_DELETE_CATEGORY("카테고리 삭제 실패"),
    GET_CATEGORIES("카테고리 조회"),
    FAILED_GET_CATEGORIES("카테고리 조회 실패"),
    UPDATE_CATEGORY("카테고리 수정"),
    FAILED_UPDATE_CATEGORY("카테고리 수정 실패");

    private final java.lang.String message;

    CategoryResponseCode(java.lang.String message) {
        this.message = message;
    }

    public java.lang.String message() {
        return message;
    }
}
