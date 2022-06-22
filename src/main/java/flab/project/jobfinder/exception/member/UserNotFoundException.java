package flab.project.jobfinder.exception.member;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String name) {
        super("name=" + name + "인 유저가 없습니다.");

    }
    public UserNotFoundException(Long id) {
        super("id=" + id + "인 유저가 없습니다.");
    }
}
