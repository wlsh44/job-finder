package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.user.LoginFailedException;
import flab.project.jobfinder.exception.user.SignUpFailedException;
import flab.project.jobfinder.exception.user.UserNotFoundException;
import flab.project.jobfinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static flab.project.jobfinder.enums.exception.LoginFailedErrorCode.NOT_EXISTS_USER;
import static flab.project.jobfinder.enums.exception.LoginFailedErrorCode.WRONG_PASSWORD;
import static flab.project.jobfinder.enums.exception.SignUpFailedErrorCode.ALREADY_EXISTS_USER;
import static flab.project.jobfinder.enums.exception.SignUpFailedErrorCode.PASSWORD_CONFIRM_NOT_CORRECT;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(LoginFormDto loginFormDto) throws LoginFailedException {
        User user = userRepository.findByEmail(loginFormDto.getEmail())
                .orElseThrow(() -> new LoginFailedException(loginFormDto, NOT_EXISTS_USER));
        if (!passwordEncoder.matches(loginFormDto.getPassword(), user.getPassword())) {
            throw new LoginFailedException(loginFormDto, WRONG_PASSWORD);
        }
        return user;
    }

    @Transactional
    public Long save(SignUpFormDto signUpFormDto) throws SignUpFailedException {
        if (userRepository.existsByEmail(signUpFormDto.getEmail())) {
            throw new SignUpFailedException(signUpFormDto, ALREADY_EXISTS_USER);
        }
        if (!signUpFormDto.getPassword().equals(signUpFormDto.getPasswordConfirm())) {
            throw new SignUpFailedException(signUpFormDto, PASSWORD_CONFIRM_NOT_CORRECT);
        }
        String password = passwordEncoder.encode(signUpFormDto.getPassword());
        User user = User.builder()
                .name(signUpFormDto.getName())
                .password(password)
                .email(signUpFormDto.getEmail())
                .build();
        return userRepository.save(user).getId();
    }

    @Transactional
    public void delete(Long id) throws UserNotFoundException {
        User user = findById(id);
        userRepository.delete(user);
    }

    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
