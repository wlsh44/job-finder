package flab.project.jobfinder.exception.user;

public class UserNotFoundException extends RuntimeException {

    private static final String ERROR_MSG = "해당 유저가 없습니다. id: %s";

    public UserNotFoundException(Long id) {
        super(ERROR_MSG.formatted(id));
    }
}
