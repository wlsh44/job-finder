package flab.project.jobfinder.exception.member;

public class SignUpFailedException extends RuntimeException {
    public SignUpFailedException(String cause) {
        super("회원가입에 실패했습니다: " + cause);
    }
}
