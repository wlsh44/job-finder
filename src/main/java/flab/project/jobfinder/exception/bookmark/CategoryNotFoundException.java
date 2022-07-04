package flab.project.jobfinder.exception.bookmark;

import lombok.Getter;

@Getter
public class CategoryNotFoundException extends RuntimeException {

    private static final String ERROR_MSG = "해당 카테고리가 존재하지 않습니다. name = %s";

    public CategoryNotFoundException(String name) {
        super(ERROR_MSG.formatted(name));
    }
}
