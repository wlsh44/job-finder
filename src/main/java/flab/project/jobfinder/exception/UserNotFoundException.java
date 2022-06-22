package flab.project.jobfinder.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("id: " + id + "인 유저가 없습니다.");
    }
}
