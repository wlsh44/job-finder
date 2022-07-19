package flab.project.jobfinder.enums.bookmark;

public enum BookmarkResponseCode implements ResponseCode {
    CREATE_BOOKMARK("북마크 생성"),
    FAILED_CREATE_BOOKMARK("북마크 생성 실패"),
    DELETE_BOOKMARK("북마크 삭제"),
    FAILED_DELETE_BOOKMARK("북마크 삭제 실패"),
    GET_BOOKMARKS("북마크 조회"),
    FAILED_GET_BOOKMARKS("북마크 조회 실패"),
    UPDATE_BOOKMARK("북마크 수정"),
    FAILED_UPDATE_BOOKMARK("북마크 수정 실패");

    private final String message;

    BookmarkResponseCode(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }
}
