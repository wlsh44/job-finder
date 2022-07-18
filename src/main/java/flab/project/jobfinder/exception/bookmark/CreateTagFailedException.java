package flab.project.jobfinder.exception.bookmark;

import flab.project.jobfinder.dto.bookmark.NewTagRequestDto;
import flab.project.jobfinder.enums.exception.TagErrorCode;

public class CreateTagFailedException extends RuntimeException{

    private static final String ERROR_MSG = "태그 생성에 실패했습니다: %s";
    private final NewTagRequestDto dto;
    private final TagErrorCode code;

    public CreateTagFailedException(NewTagRequestDto dto, TagErrorCode code) {
        super(ERROR_MSG.formatted(code.errorMsg()));
        this.dto = dto;
        this.code = code;
    }
}
