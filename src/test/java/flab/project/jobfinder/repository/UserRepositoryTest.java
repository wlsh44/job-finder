package flab.project.jobfinder.repository;

import flab.project.jobfinder.dto.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
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
        User saveUser = memberRepository.save(user);

        //when
        User result = memberRepository.findById(saveUser.getId()).get();

        //then
        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }
}