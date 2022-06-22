package flab.project.jobfinder.repository;

import flab.project.jobfinder.dto.member.Members;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

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
        Members member = Members.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();
        Members saveMember = memberRepository.save(member);

        //when
        Members result = memberRepository.findById(saveMember.getId()).get();

        //then
        assertThat(result.getName()).isEqualTo(member.getName());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }
}