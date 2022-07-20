package flab.project.jobfinder.exception.bookmark;

import flab.project.jobfinder.enums.bookmark.ResponseCode;
import flab.project.jobfinder.enums.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BookmarkException extends RuntimeException {

    private static final String errorMsg = "%s: %s.";
    private static final String errorMsgName = "%s: %s. name = %s";
    private static final String errorMsgId = "%s: %s. id = %d";
    private final ResponseCode status;
    private final ErrorCode code;

    public BookmarkException(ResponseCode status, ErrorCode code) {
        super(errorMsg.formatted(status.message(), code.errorMsg()));
        this.status = status;
        this.code = code;
    }

    public BookmarkException(ResponseCode status, ErrorCode code, String name) {
        super(errorMsgName.formatted(status.message(), code.errorMsg(), name));
        this.status = status;
        this.code = code;
    }

    public BookmarkException(ResponseCode status, ErrorCode code, Long id) {
        super(errorMsgId.formatted(status.message(), code.errorMsg(), id));
        this.status = status;
        this.code = code;
    }
}
