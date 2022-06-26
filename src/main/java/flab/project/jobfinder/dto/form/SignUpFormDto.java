package flab.project.jobfinder.dto.form;

import flab.project.jobfinder.annotation.UniqueEmail;
import lombok.*;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpFormDto {

    @Pattern(message = "4자 이상 10자 미만, 영어 숫자만", regexp = "^[a-zA-Z0-9]{4,20}")
    @NotBlank(message = "아이디를 입력해주세요")
    private String name;

    @Pattern(message = "4자 이상 10자 미만, 영어 숫자만", regexp = "^[a-zA-Z0-9]{4,20}")
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String passwordConfirm;

    private boolean passwordCheck;

    @UniqueEmail
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @AssertTrue(message = "비밀번호가 일치하지 않습니다.")
    public boolean isPasswordCheck() {
        try {
            return password.equals(passwordConfirm);
        } catch (Exception e) {
            return false;
        }
    }
}
