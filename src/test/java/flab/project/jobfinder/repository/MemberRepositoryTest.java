package flab.project.jobfinder.repository;

import flab.project.jobfinder.dto.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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
        Member member = Member.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();
        Member saveMember = memberRepository.save(member);

        //when
        Member result = memberRepository.findById(saveMember.getId()).get();

        //then
        assertThat(result.getName()).isEqualTo(member.getName());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }
}