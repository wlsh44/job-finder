package flab.project.jobfinder.dto.form;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginFormDto {
    @Email
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
}
