package flab.project.jobfinder.dto.form;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
public class LoginFormDto {
    @NotNull(message = "아이디를 입력해주세요")
    private String name;
    @NotNull(message = "비밀번호를 입력해주세요")
    private String password;
}
