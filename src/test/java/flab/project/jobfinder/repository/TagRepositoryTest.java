package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.recruit.Tag;
import flab.project.jobfinder.entity.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static flab.project.jobfinder.enums.JobType.FULL_TIME;
import static flab.project.jobfinder.enums.Location.GANGNAM;
import static flab.project.jobfinder.enums.Platform.JOBKOREA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TagRepositoryTest {

    @Autowired
    TagRepository tagRepository;

    Tag tag;
    String tagName;

    @BeforeEach
    void init() {
        tagName = "tag";
        tag = Tag.builder()
                .name(tagName)
                .recruitTagList(new ArrayList<>())
                .build();
        tag = tagRepository.save(tag);
    }

    @Test
    @DisplayName("이름으로 태그 조회")
    void findByName() {
        //given

        //when
        Optional<Tag> tag = tagRepository.findByName(tagName);

        //then
        assertThat(tag).isEqualTo(Optional.of(this.tag));
    }
}