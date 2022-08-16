package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User saveUser;
    String email;

    @BeforeEach
    void init() {
        String name = "test";
        String password = "password";
        email = "email@email.email";
        User user = User.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();
        saveUser = userRepository.save(user);
    }

    @Test
    @DisplayName("조회 테스트 - 아이디")
    void findByIdTest_Id() {
        //given

        //when
        User result = userRepository.findById(saveUser.getId()).get();

        //then
        assertThat(result).isEqualTo(saveUser);
    }

    @Test
    @DisplayName("조회 테스트 - 이메일")
    void findByIdTest_Email() {
        //given

        //when
        Optional<User> result = userRepository.findByEmail(email);

        //then
        assertThat(result).isEqualTo(Optional.of(saveUser));
    }

    @Test
    @DisplayName("이메일로 유저 여부 확인 - 있는 경우")
    void existsByEmailTest_Exist() {
        //given

        //when
        boolean res = userRepository.existsByEmail(email);

        //then
        assertThat(res).isTrue();
    }

    @Test
    @DisplayName("이메일로 유저 여부 확인 - 없는 경우")
    void existsByEmailTest_NotExist() {
        //given
        String notExistEmail = "없는@이메일.주소";

        //when
        boolean res = userRepository.existsByEmail(notExistEmail);

        //then
        assertThat(res).isFalse();
    }
}