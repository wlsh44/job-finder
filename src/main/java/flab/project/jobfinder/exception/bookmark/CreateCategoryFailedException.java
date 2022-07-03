package flab.project.jobfinder.exception.bookmark;

import flab.project.jobfinder.dto.bookmark.NewCategoryRequestDto;
import flab.project.jobfinder.enums.exception.CreateCategoryFailedErrorCode;
import lombok.Getter;

@Getter
public class CreateCategoryFailedException extends RuntimeException {

    public static final String ERROR_MSG = "카테고리 생성에 실패했습니다: %s";
    private final NewCategoryRequestDto dto;
    private final CreateCategoryFailedErrorCode code;

    public CreateCategoryFailedException(NewCategoryRequestDto dto, CreateCategoryFailedErrorCode code) {
        super(ERROR_MSG.formatted(code.errorMsg()));
        this.dto = dto;
        this.code = code;
    }
}
