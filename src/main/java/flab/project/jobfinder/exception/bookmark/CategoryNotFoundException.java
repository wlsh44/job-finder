package flab.project.jobfinder.exception.bookmark;

import lombok.Getter;

@Getter
public class CategoryNotFoundException extends RuntimeException {

    private static final String ERROR_MSG_NAME = "해당 카테고리가 존재하지 않습니다. name = %s";
    private static final String ERROR_MSG_ID = "해당 카테고리가 존재하지 않습니다. id = %d";

    public CategoryNotFoundException(String name) {
        super(ERROR_MSG_NAME.formatted(name));
    }

    public CategoryNotFoundException(Long id) {
        super(ERROR_MSG_ID.formatted(id));
    }
}
