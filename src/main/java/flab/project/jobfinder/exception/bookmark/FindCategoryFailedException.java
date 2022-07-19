package flab.project.jobfinder.exception.bookmark;

import flab.project.jobfinder.enums.exception.ErrorCode;
import lombok.Getter;

@Getter
public class FindCategoryFailedException extends RuntimeException {

    public FindCategoryFailedException(ErrorCode code, Long categoryId) {
        super(code.errorMsg().formatted(categoryId));
    }

    public FindCategoryFailedException(ErrorCode code, String name) {
        super(code.errorMsg().formatted(name));
    }
}
