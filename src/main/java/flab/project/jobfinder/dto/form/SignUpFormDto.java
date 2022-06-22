package flab.project.jobfinder.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@Getter
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

    @Email
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
}
