package flab.project.jobfinder.repository;

import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.recruit.RecruitTag;
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
class RecruitTagRepositoryTest {

    @Autowired
    RecruitTagRepository recruitTagRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    RecruitRepository recruitRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    User user;
    Category saveCategory;
    Recruit saveRecruit;
    Tag saveTag;
    RecruitTag recruitTag;

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

        //태그 저장
        Tag tag = Tag.builder()
                .name("testTagName")
                .recruitTagList(new ArrayList<>())
                .build();
        saveTag = tagRepository.save(tag);

        //태깅
        recruitTag = RecruitTag.builder()
                .tag(saveTag)
                .recruit(saveRecruit)
                .build();
        recruitTag = recruitTagRepository.save(recruitTag);
        saveTag.getRecruitTagList().add(recruitTag);
        saveRecruit.getRecruitTagList().add(recruitTag);
    }


    @Test
    @DisplayName("북마크 아이디와 태그 이름으로 존재 여부 확인 - 있는 경우")
    void existsByRecruit_IdAndTag_Name_Exist() {
        //given

        //when
        boolean res = recruitTagRepository.existsByRecruit_IdAndTag_Name(saveRecruit.getId(), saveTag.getName());

        //then
        assertThat(res).isTrue();
    }

    @Test
    @DisplayName("북마크 아이디와 태그 이름으로 존재 여부 확인 - 없는 경우")
    void existsByRecruit_IdAndTag_Name_NotExist() {
        //given
        String notExistTagName = "없는 태그 이름";

        //when
        boolean res = recruitTagRepository.existsByRecruit_IdAndTag_Name(saveRecruit.getId(), notExistTagName);

        //then
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("태깅 조회")
    void findRecruitTag() {
        //given

        //when
        Optional<RecruitTag> recruitTag = recruitTagRepository.findRecruitTag(saveRecruit.getId(), saveTag.getId());

        //then
        assertThat(recruitTag).isEqualTo(Optional.of(this.recruitTag));
    }

    @Test
    @DisplayName("태깅 수 조회")
    void countRecruitTag() {
        //given

        //when
        int recruitTagCnt = recruitTagRepository.countRecruitTag(saveTag.getId());

        //then
        assertThat(recruitTagCnt).isEqualTo(1);
    }
}