package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.form.LoginFormDto;
import flab.project.jobfinder.dto.form.SignUpFormDto;
import flab.project.jobfinder.dto.user.User;
import flab.project.jobfinder.exception.member.LoginFailedException;
import flab.project.jobfinder.exception.member.SignUpFailedException;
import flab.project.jobfinder.exception.member.UserNotFoundException;
import flab.project.jobfinder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(LoginFormDto loginFormDto) {
        User user = memberRepository.findByEmail(loginFormDto.getEmail())
                .orElseThrow(() -> new LoginFailedException("존재하지 않는 유저"));
        if (!passwordEncoder.matches(loginFormDto.getPassword(), user.getPassword())) {
            throw new LoginFailedException("비밀번호 틀림");
        }
        return user;
    }

    @Transactional
    public Long save(SignUpFormDto signUpFormDto) {
        if (memberRepository.existsByEmail(signUpFormDto.getEmail())) {
            throw new SignUpFailedException("이미 존재하는 유저");
        }
        if (!signUpFormDto.getPassword().equals(signUpFormDto.getPasswordConfirm())) {
            throw new SignUpFailedException("비밀번호 검증 실패");
        }
        //TODO
        //비밀번호 암호화
        String password = passwordEncoder.encode(signUpFormDto.getPassword());
        User user = User.builder()
                .name(signUpFormDto.getName())
                .password(password)
                .email(signUpFormDto.getEmail())
                .build();
        return memberRepository.save(user).getId();
    }

    @Transactional
    public void delete(Long id) {
        User user = findById(id);
        memberRepository.delete(user);
    }

    public User findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
