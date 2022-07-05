package flab.project.jobfinder.exception.user;

public class UserNotFoundException extends RuntimeException {

    public static final String FAILED_MSG = "해당 유저가 없습니다. id: %s";

    public UserNotFoundException(Long id) {
        super(FAILED_MSG.formatted(id));
    }
}
