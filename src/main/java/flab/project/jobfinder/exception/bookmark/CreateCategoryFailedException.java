package flab.project.jobfinder.exception.bookmark;

import flab.project.jobfinder.enums.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CreateCategoryFailedException extends RuntimeException {

    public CreateCategoryFailedException(ErrorCode code, String name) {
        super(code.errorMsg().formatted(name));
    }
}
