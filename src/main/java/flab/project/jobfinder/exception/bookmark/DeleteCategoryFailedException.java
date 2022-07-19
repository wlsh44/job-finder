package flab.project.jobfinder.exception.bookmark;

import flab.project.jobfinder.enums.exception.ErrorCode;
import lombok.Getter;

@Getter
public class DeleteCategoryFailedException extends RuntimeException {

    public DeleteCategoryFailedException(ErrorCode code, Long categoryId) {
        super(code.errorMsg().formatted(categoryId));
    }
}
