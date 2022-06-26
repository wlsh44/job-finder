package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("조회 테스트")
    void findByIdTest() {
        //given
        String name = "test";
        String password = "password";
        String email = "email@email.email";
        User user = User.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();
        User saveUser = userRepository.save(user);

        //when
        User result = userRepository.findById(saveUser.getId()).get();

        //then
        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }
}