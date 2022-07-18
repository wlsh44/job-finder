package flab.project.jobfinder.exception.bookmark;

import flab.project.jobfinder.dto.bookmark.NewBookmarkRequestDto;
import flab.project.jobfinder.enums.exception.BookmarkErrorCode;
import lombok.Getter;

@Getter
public class CreateBookmarkFailedException extends RuntimeException {

    private static final String ERROR_MSG = "북마크 생성에 실패했습니다: %s";
    private final NewBookmarkRequestDto dto;
    private final BookmarkErrorCode code;

    public CreateBookmarkFailedException(NewBookmarkRequestDto dto, BookmarkErrorCode code) {
        super(ERROR_MSG.formatted(code.errorMsg()));
        this.dto = dto;
        this.code = code;
    }
}
