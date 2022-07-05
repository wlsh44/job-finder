package flab.project.jobfinder.exception.bookmark;

import lombok.Getter;

@Getter
public class BookmarkNotFoundException extends RuntimeException {

    private static final String ERROR_MSG = "해당 북마크가 존재하지 않습니다. id = %d";

    public BookmarkNotFoundException(Long id) {
        super(ERROR_MSG.formatted(id));
    }
}
