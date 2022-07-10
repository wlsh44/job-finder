package flab.project.jobfinder.service.user.validator;

import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import static flab.project.jobfinder.enums.exception.LoginFailedErrorCode.NOT_EXISTS_USER;
import static flab.project.jobfinder.enums.exception.LoginFailedErrorCode.WRONG_PASSWORD;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginValidator implements Validator {

    private final static String errorMsg = "아이디 또는 비밀번호가 틀렸습니다.";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return LoginFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BindingResult bindingResult = (BindingResult) errors;
        LoginFormDto loginFormDto = (LoginFormDto) target;
        User user = userRepository.findByEmail(loginFormDto.getEmail());
        if (user == null) {
            bindingResult.addError(new ObjectError("loginFormDto", new String[]{NOT_EXISTS_USER.name()}, null, errorMsg));
            log.info("존재하지 않는 유저");
            return;
        }
//        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginFormDto.getPassword(), user.getPassword())) {
            bindingResult.addError(new ObjectError("loginFormDto", new String[]{WRONG_PASSWORD.name()}, null, errorMsg));
            log.info("비밀번호 오류");
        }
    }
}
