package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static flab.project.jobfinder.enums.JobType.FULL_TIME;
import static flab.project.jobfinder.enums.Location.GANGNAM;
import static flab.project.jobfinder.enums.Platform.JOBKOREA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RecruitRepositoryTest {

    @Autowired
    RecruitRepository recruitRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    User user;
    Category saveCategory;
    Recruit saveRecruit;

    @BeforeEach
    void init() {
        //유저 저장
        user = User.builder()
                .name("test1")
                .password("password")
                .email("test1@test.test")
                .categories(new ArrayList<>())
                .recruits(new ArrayList<>())
                .build();
        user = userRepository.save(user);

        //카테고리 저장
        Category category = Category.builder()
                .user(user)
                .name("testCategory")
                .recruits(new ArrayList<>())
                .build();
        saveCategory = categoryRepository.save(category);

        //북마크 저장
        Recruit recruit = Recruit.builder()
                .career("career")
                .corp("corp")
                .dueDate(LocalDate.now().plusDays(1))
                .isAlwaysRecruiting(false)
                .jobType(FULL_TIME.name())
                .location(GANGNAM.district())
                .platform(JOBKOREA)
                .techStack("tech")
                .title("title")
                .url("url")
                .recruitTagList(new ArrayList<>())
                .category(saveCategory)
                .user(user)
                .build();
        saveRecruit = recruitRepository.save(recruit);
        saveCategory.getRecruits().add(saveRecruit);
    }

    @Test
    @DisplayName("채용 공고 조회 - 북마크 아이디")
    void findRecruit_Id() {
        //given

        //when
        Recruit recruit = recruitRepository.findRecruit(user, saveRecruit.getId());

        //then
        assertThat(recruit).isEqualTo(saveRecruit);
    }

    @Test
    @DisplayName("채용 공고 조회 - 카테고리 아이디, 북마크 아이디")
    void findRecruit_CategoryId_BookmarkId() {
        //given

        //when
        Recruit recruit = recruitRepository.findRecruit(user, saveCategory.getId(), saveRecruit.getId());

        //then
        assertThat(recruit).isEqualTo(saveRecruit);
    }

    @Test
    @DisplayName("카테고리에 해당하는 북마크 조회")
    void findRecruits() {
        //given
        Pageable pageable = PageRequest.of(0, 20);
        Page<Recruit> expect = new PageImpl<>(List.of(saveRecruit), pageable, 1);

        //when
        Page<Recruit> recruits = recruitRepository.findRecruits(user, saveCategory.getId(), pageable);

        //then
        assertThat(recruits).isEqualTo(expect);
    }
}