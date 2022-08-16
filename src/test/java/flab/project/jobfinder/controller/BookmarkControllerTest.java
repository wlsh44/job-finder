package flab.project.jobfinder.controller;

import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.repository.CategoryRepository;
import flab.project.jobfinder.repository.RecruitRepository;
import flab.project.jobfinder.repository.UserRepository;
import flab.project.jobfinder.service.user.BookmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static flab.project.jobfinder.enums.bookmark.CategoryResponseCode.FAILED_CREATE_CATEGORY;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookmarkController.class)
class BookmarkControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookmarkService bookmarkService;

    @MockBean
    RecruitRepository recruitRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    void init() {
        User user = User.builder()
                .name("test")
                .email("test@test.test")
                .password("test")
                .build();
        userRepository.save(user);
    }

    @Nested
    @DisplayName("카테고리")
    class CategoryTest {

        @Test
        @DisplayName("카테고리 생성")
        void createCategoryTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/category")
                            .param("name", "categoryName"))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection());
        }

        @Test
        @DisplayName("카테고리 생성 실패 - 카테고리 이름 입력 안 한 경우")
        void createCategoryFailTest_NoCategoryName() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/category")
                            .param("name", ""))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection());
//                    .andExpect(jsonPath("$.message", is(FAILED_CREATE_CATEGORY.message())));
        }
    }
}