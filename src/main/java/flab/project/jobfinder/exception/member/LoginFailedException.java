package flab.project.jobfinder.exception.member;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException() {
        super("로그인에 실패했습니다.");
    }
}
