package flab.project.jobfinder.exception.bookmark;

import flab.project.jobfinder.enums.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UnTaggingFailedException extends RuntimeException {

//    private static final String ERROR_MSG_ID = "해당 태그가 존재하지 않습니다. id = %d";

    public UnTaggingFailedException(ErrorCode code, String tagName) {
        super(code.errorMsg().formatted(tagName));
    }

    public UnTaggingFailedException(ErrorCode code, Long id) {
        super(code.errorMsg().formatted(id));
    }
}
