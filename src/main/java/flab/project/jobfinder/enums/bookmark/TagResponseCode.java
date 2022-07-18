package flab.project.jobfinder.enums.bookmark;

public enum TagResponseCode {
    CREATE_TAG("태그 생성"),
    FAILED_CREATE_TAG("태그 생성 실패"),
    TAGGING("태그 추가"),
    FAILED_TAGGING("태그 추가 실패"),
    UNTAGGING("태그 제거"),
    FAILED_UNTAGGING("태그 제거 실패"),
    DELETE_TAG("태그 삭제"),
    FAILED_DELETE_TAG("태그 삭제 실패"),
    GET_TAG("태그 조회"),
    FAILED_GET_TAG("태그 조회 실패"),
    UPDATE_TAG("태그 수정"),
    FAILED_UPDATE_TAG("태그 수정 실패");

    private final String message;

    TagResponseCode(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
