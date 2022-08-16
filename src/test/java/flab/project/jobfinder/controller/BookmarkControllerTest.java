package flab.project.jobfinder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.dto.page.PageDto;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.exception.bookmark.BookmarkException;
import flab.project.jobfinder.exception.bookmark.CategoryException;
import flab.project.jobfinder.exception.bookmark.TagException;
import flab.project.jobfinder.service.user.BookmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static flab.project.jobfinder.consts.SessionConst.LOGIN_SESSION_ID;
import static flab.project.jobfinder.enums.JobType.FULL_TIME;
import static flab.project.jobfinder.enums.Location.GANGNAM;
import static flab.project.jobfinder.enums.PayType.ANNUAL;
import static flab.project.jobfinder.enums.Platform.JOBKOREA;
import static flab.project.jobfinder.enums.bookmark.BookmarkResponseCode.*;
import static flab.project.jobfinder.enums.bookmark.CategoryResponseCode.FAILED_CREATE_CATEGORY;
import static flab.project.jobfinder.enums.bookmark.TagResponseCode.*;
import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.BOOKMARK_ID_NOT_FOUND;
import static flab.project.jobfinder.enums.exception.BookmarkErrorCode.REQUIRED_AT_LEAST_ONE_CATEGORY;
import static flab.project.jobfinder.enums.exception.CategoryErrorCode.*;
import static flab.project.jobfinder.enums.exception.TagErrorCode.TAG_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookmarkController.class)
class BookmarkControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookmarkService bookmarkService;

    MockHttpSession session;

    User user;

    @BeforeEach
    void init() {
        user = User.builder()
                .name("test")
                .email("test@test.test")
                .password("test")
                .build();
        session = new MockHttpSession();
        session.setAttribute(LOGIN_SESSION_ID, user);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Nested
    @DisplayName("카테고리")
    class CategoryTest {

        @Test
        @DisplayName("카테고리 생성")
        void createCategoryTest() throws Exception {
            //given
            String categoryName = "categoryName";
            NewCategoryRequestDto dto = new NewCategoryRequestDto();
            dto.setName(categoryName);
            List<CategoryResponseDto> categoryList = List.of(
                    new CategoryResponseDto(1L, categoryName)
            );
            given(bookmarkService.createCategory(user, dto))
                    .willReturn(categoryList);

            //when then
            mockMvc.perform(post("/category")
                            .session(session)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(dto)))
                    .andDo(print())
                    .andExpect(model().attribute("categoryList", categoryList))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("카테고리 생성 실패 - 카테고리 이름 입력 안 한 경우")
        void createCategoryTest_NoCategoryName_Fail() throws Exception {
            //given
            NewCategoryRequestDto dto = new NewCategoryRequestDto();

            //when then
            MvcResult mvcResult = mockMvc.perform(post("/category")
                            .session(session)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(dto)))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            String responseMsg = toResponseMsg(mvcResult);

            assertThat(responseMsg).isEqualTo(FAILED_CREATE_CATEGORY.message());
        }

        @Test
        @DisplayName("카테고리 생성 실패 - 이미 있는 카테고리 이름일 경우")
        void createCategoryTest_ExistCategoryName_Fail() throws Exception {
            //given
            String categoryName = "categoryName";
            NewCategoryRequestDto dto = new NewCategoryRequestDto();
            dto.setName(categoryName);
            given(bookmarkService.createCategory(user, dto))
                    .willThrow(new CategoryException(FAILED_CREATE_CATEGORY, ALREADY_EXISTS_CATEGORY));

            //when then
            MvcResult mvcResult = mockMvc.perform(post("/category")
                            .session(session)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(dto)))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            String responseMsg = toResponseMsg(mvcResult);

            assertThat(responseMsg).isEqualTo(new CategoryException(FAILED_CREATE_CATEGORY, ALREADY_EXISTS_CATEGORY).getMessage());
        }

        @Test
        @DisplayName("카테고리 생성 실패 - 10개 초과할 경우")
        void createCategoryTest_OverMaximumCnt_Fail() throws Exception {
            //given
            String categoryName = "categoryName";
            NewCategoryRequestDto dto = new NewCategoryRequestDto();
            dto.setName(categoryName);
            given(bookmarkService.createCategory(user, dto))
                    .willThrow(new CategoryException(FAILED_CREATE_CATEGORY, TOO_MANY_CATEGORIES));

            //when then
            MvcResult mvcResult = mockMvc.perform(post("/category")
                            .session(session)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(dto)))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            String responseMsg = toResponseMsg(mvcResult);

            assertThat(responseMsg).isEqualTo(new CategoryException(FAILED_CREATE_CATEGORY, TOO_MANY_CATEGORIES).getMessage());
        }

        @Test
        @DisplayName("모달 카테고리 조회")
        void modalCategoryList() throws Exception {
            //given
            List<CategoryResponseDto> categoryList = List.of(
                    new CategoryResponseDto(1L, "categoryName")
            );
            given(bookmarkService.findAllCategoryByUser(user))
                    .willReturn(categoryList);

            //when then
            mockMvc.perform(get("/category")
                            .session(session))
                    .andDo(print())
                    .andExpect(model().attribute("categoryList", categoryList))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("마이페이지 카테고리 조회")
        void myPageCategoryList() throws Exception {
            //given
            List<CategoryResponseDto> categoryList = List.of(
                    new CategoryResponseDto(1L, "categoryName")
            );
            given(bookmarkService.findAllCategoryByUser(user))
                    .willReturn(categoryList);

            //when then
            mockMvc.perform(get("/my-page/bookmark")
                            .session(session))
                    .andDo(print())
                    .andExpect(model().attribute("categoryList", categoryList))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("카테고리 삭제")
        void deleteCategory() throws Exception {
            //given
            Long categoryId = 1L;
            List<CategoryResponseDto> categoryList = List.of();
            given(bookmarkService.deleteCategory(user, categoryId))
                    .willReturn(categoryList);

            //when then
            mockMvc.perform(delete("/my-page/bookmark/")
                            .param("categoryId", categoryId.toString())
                            .session(session))
                    .andDo(print())
                    .andExpect(model().attribute("categoryList", categoryList))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("카테고리 삭제 실패 - 없는 카테고리 아이디")
        void deleteCategory_NotExistCategory_Fail() throws Exception {
            //given
            Long categoryId = 1L;
            given(bookmarkService.deleteCategory(user, categoryId))
                    .willThrow(new CategoryException(FAILED_DELETE_BOOKMARK, CATEGORY_ID_NOT_FOUND, categoryId));

            //when then
            MvcResult mvcResult = mockMvc.perform(delete("/my-page/bookmark/")
                            .param("categoryId", categoryId.toString())
                            .session(session))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            String responseMsg = toResponseMsg(mvcResult);

            assertThat(responseMsg).isEqualTo(new CategoryException(FAILED_DELETE_BOOKMARK, CATEGORY_ID_NOT_FOUND, categoryId).getMessage());
        }
    }

    @Nested
    @DisplayName("북마크")
    class BookmarkTest {

        String categoryName;
        Long categoryId;
        RecruitDto recruitDto;

        @BeforeEach
        void init() {
            categoryName = "categoryName";
            categoryId = 1L;
            recruitDto = RecruitDto.builder()
                    .career("career")
                    .corp("corp")
                    .dueDate(LocalDate.now().plusDays(1))
                    .isAlwaysRecruiting(false)
                    .jobType(FULL_TIME.name())
                    .location(GANGNAM.district())
                    .pay(ANNUAL.name())
                    .platform(JOBKOREA.koreaName())
                    .techStack("tech")
                    .title("title")
                    .url("url")
                    .build();
        }

        @Test
        @DisplayName("북마크 리스트 조회")
        void bookmarkList() throws Exception {
            //given
            Integer pageNum = 1;
            BookmarkPageDto dto = BookmarkPageDto.builder()
                    .bookmarkList(List.of(
                            new BookmarkResponseDto(1L, recruitDto, categoryName, null)))
                    .pageDto(PageDto.builder()
                            .totalPage(1)
                            .startPage(1)
                            .build())
                    .build();
            Pageable pageable = PageRequest.of(0, 20);
            given(bookmarkService.findBookmarkByCategory(user, categoryId, pageable))
                    .willReturn(dto);

            //when then
            mockMvc.perform(get("/my-page/bookmark/1")
                            .session(session)
                            .param("page", "1"))
                    .andDo(print())
                    .andExpect(model().attribute("categoryId", categoryId))
                    .andExpect(model().attribute("bookmarkList", dto.getBookmarkList()))
                    .andExpect(model().attribute("currentPage", pageNum))
                    .andExpect(model().attribute("startPage", dto.getPageDto().getStartPage()))
                    .andExpect(model().attribute("totalPage", dto.getPageDto().getTotalPage()))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("북마크 리스트 조회 - 페이지 지정 안 한 경우")
        void bookmarkList_PageIsNull() throws Exception {
            //given
            BookmarkPageDto dto = BookmarkPageDto.builder()
                    .bookmarkList(List.of(
                            new BookmarkResponseDto(1L, recruitDto, categoryName, null)))
                    .pageDto(PageDto.builder()
                            .totalPage(1)
                            .startPage(1)
                            .build())
                    .build();
            Pageable pageable = PageRequest.of(0, 20);
            given(bookmarkService.findBookmarkByCategory(user, categoryId, pageable))
                    .willReturn(dto);

            //when then
            mockMvc.perform(get("/my-page/bookmark/1")
                            .session(session))
                    .andDo(print())
                    .andExpect(model().attribute("categoryId", categoryId))
                    .andExpect(model().attribute("bookmarkList", dto.getBookmarkList()))
                    .andExpect(model().attribute("currentPage", 1))
                    .andExpect(model().attribute("startPage", dto.getPageDto().getStartPage()))
                    .andExpect(model().attribute("totalPage", dto.getPageDto().getTotalPage()))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("북마크 리스트 조회 - 페이지 값 이상한 경우")
        void bookmarkList_WeirdPageValue() throws Exception {
            //given
            Integer page = -51;
            BookmarkPageDto dto = BookmarkPageDto.builder()
                    .bookmarkList(List.of(
                            new BookmarkResponseDto(1L, recruitDto, categoryName, null)))
                    .pageDto(PageDto.builder()
                            .totalPage(1)
                            .startPage(1)
                            .build())
                    .build();
            Pageable pageable = PageRequest.of(0, 20);
            given(bookmarkService.findBookmarkByCategory(user, categoryId, pageable))
                    .willReturn(dto);

            //when then
            mockMvc.perform(get("/my-page/bookmark/1")
                            .session(session)
                            .param("page", page.toString()))
                    .andDo(print())
                    .andExpect(model().attribute("categoryId", categoryId))
                    .andExpect(model().attribute("bookmarkList", dto.getBookmarkList()))
                    .andExpect(model().attribute("currentPage", 1))
                    .andExpect(model().attribute("startPage", dto.getPageDto().getStartPage()))
                    .andExpect(model().attribute("totalPage", dto.getPageDto().getTotalPage()))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("북마크")
        void bookmarkRecruit() throws Exception {
            //given
            NewBookmarkRequestDto dto = new NewBookmarkRequestDto();
            dto.setRecruitDto(recruitDto);
            dto.setCategoryList(List.of(categoryName));
            List<BookmarkResponseDto> responseDto = List.of(new BookmarkResponseDto(1L, recruitDto, categoryName, null));
            String jsonDto = toJson(dto);
            given(bookmarkService.bookmark(user, dto))
                    .willReturn(responseDto);

            //when then
            MvcResult mvcResult = mockMvc.perform(post("/bookmark")
                            .session(session)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonDto))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String stringMsg = toResponseMsg(mvcResult);

            assertThat(stringMsg).isEqualTo(CREATE_BOOKMARK.message());
        }

        @Test
        @DisplayName("북마크 실패 - 카테고리 지정 안 한 경우")
        void bookmarkRecruit_Fail() throws Exception {
            //given
            NewBookmarkRequestDto dto = new NewBookmarkRequestDto();
            dto.setRecruitDto(recruitDto);
            dto.setCategoryList(List.of());
            String jsonDto = toJson(dto);
            given(bookmarkService.bookmark(user, dto))
                    .willThrow(new BookmarkException(FAILED_CREATE_BOOKMARK, REQUIRED_AT_LEAST_ONE_CATEGORY));

            //when then
            MvcResult mvcResult = mockMvc.perform(post("/bookmark")
                            .session(session)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonDto))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            String stringMsg = toResponseMsg(mvcResult);

            assertThat(stringMsg).isEqualTo(new BookmarkException(FAILED_CREATE_BOOKMARK, REQUIRED_AT_LEAST_ONE_CATEGORY).getMessage());
        }

        @Test
        @DisplayName("북마크 삭제")
        void unbookmark() throws Exception {
            //given
            Integer pageNum = 1;
            Pageable pageable = PageRequest.of(0, 20);
            Long bookmarkId = 1L;
            PageDto pageDto = PageDto.builder()
                    .totalPage(1)
                    .startPage(1)
                    .build();
            BookmarkPageDto dto = new BookmarkPageDto(List.of(),
                    pageDto
            );
            given(bookmarkService.unbookmark(user, categoryId, bookmarkId, pageable))
                    .willReturn(dto);

            //when then
            mockMvc.perform(delete("/my-page/bookmark/1")
                            .session(session)
                    .param("bookmarkId", bookmarkId.toString())
                    .param("page", pageNum.toString()))
                    .andExpect(model().attribute("categoryId", categoryId))
                    .andExpect(model().attribute("bookmarkList", dto.getBookmarkList()))
                    .andExpect(model().attribute("currentPage", 1))
                    .andExpect(model().attribute("startPage", dto.getPageDto().getStartPage()))
                    .andExpect(model().attribute("totalPage", dto.getPageDto().getTotalPage()))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("북마크 삭제 실패 - 없는 북마크")
        void unbookmark_Fail() throws Exception {
            //given
            Integer pageNum = 1;
            Pageable pageable = PageRequest.of(0, 20);
            Long bookmarkId = 1L;
            given(bookmarkService.unbookmark(user, categoryId, bookmarkId, pageable))
                    .willThrow(new BookmarkException(FAILED_DELETE_BOOKMARK, BOOKMARK_ID_NOT_FOUND, bookmarkId));

            //when then
            MvcResult mvcResult = mockMvc.perform(delete("/my-page/bookmark/1")
                            .session(session)
                            .param("bookmarkId", bookmarkId.toString())
                            .param("page", pageNum.toString()))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            String responseMsg = toResponseMsg(mvcResult);

            assertThat(responseMsg).isEqualTo(new BookmarkException(FAILED_DELETE_BOOKMARK, BOOKMARK_ID_NOT_FOUND, bookmarkId).getMessage());
        }
    }

    @Nested
    @DisplayName("태그")
    class TagTest {

        Long bookmarkId;

        @BeforeEach
        void init() {
            bookmarkId = 1L;
        }

        @Test
        @DisplayName("태깅")
        void tagging() throws Exception{
            //given
            TaggingRequestDto dto = new TaggingRequestDto();
            dto.setTagName("tag");
            TagResponseDto responseDto = new TagResponseDto(1L, "tag");
            given(bookmarkService.tagging(user, dto, bookmarkId))
                    .willReturn(responseDto);

            //when then
            MvcResult mvcResult = mockMvc.perform(post("/tag")
                            .param("bookmarkId", this.bookmarkId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(dto))
                            .session(session))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String responseMsg = toResponseMsg(mvcResult);

            assertThat(responseMsg).isEqualTo(TAGGING.message());
        }

        @Test
        @DisplayName("태깅 실패 - 북마크 없는 경우")
        void tagging_Fail() throws Exception {
            //given
            TaggingRequestDto dto = new TaggingRequestDto();
            dto.setTagName("tag");
            given(bookmarkService.tagging(user, dto, bookmarkId))
                    .willThrow(new TagException(FAILED_TAGGING, BOOKMARK_ID_NOT_FOUND, bookmarkId));

            //when then
            MvcResult mvcResult = mockMvc.perform(post("/tag")
                            .param("bookmarkId", this.bookmarkId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(dto))
                            .session(session))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            String responseMsg = toResponseMsg(mvcResult);

            assertThat(responseMsg).isEqualTo(new TagException(FAILED_TAGGING, BOOKMARK_ID_NOT_FOUND, bookmarkId).getMessage());
        }

        @Test
        @DisplayName("태그 삭제")
        void untagging() throws Exception{
            //given
            UnTaggingRequestDto dto = new UnTaggingRequestDto();
            dto.setTagId("1");

            //when then
            MvcResult mvcResult = mockMvc.perform(delete("/bookmark/tag")
                            .param("bookmarkId", bookmarkId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(dto))
                            .session(session))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String responseMsg = toResponseMsg(mvcResult);

            assertThat(responseMsg).isEqualTo(UNTAGGING.message());
        }

        @Test
        @DisplayName("태그 삭제 실패 - 태깅이 안되어 있는 경우")
        void untagging_NotExistTag_Fail() throws Exception {
            //given
            Long tagId = 1L;
            UnTaggingRequestDto dto = new UnTaggingRequestDto();
            dto.setTagId("1");
            doThrow(new TagException(FAILED_UNTAGGING, TAG_NOT_FOUND, tagId))
                    .when(bookmarkService).untagging(user, dto, bookmarkId);
            //when then
            MvcResult mvcResult = mockMvc.perform(delete("/bookmark/tag")
                            .param("bookmarkId", this.bookmarkId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(dto))
                            .session(session))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            String responseMsg = toResponseMsg(mvcResult);

            assertThat(responseMsg).isEqualTo(new TagException(FAILED_UNTAGGING, TAG_NOT_FOUND, tagId).getMessage());
        }

        @Test
        @DisplayName("태그 삭제 실패 - 태깅이 안되어 있는 경우")
        void untagging_NotUserTag_Fail() throws Exception {
            //given
            Long tagId = 1L;
            UnTaggingRequestDto dto = new UnTaggingRequestDto();
            dto.setTagId("1");
            doThrow(new TagException(FAILED_UNTAGGING, BOOKMARK_ID_NOT_FOUND, tagId))
                    .when(bookmarkService).untagging(user, dto, bookmarkId);

            //when then
            MvcResult mvcResult = mockMvc.perform(delete("/bookmark/tag")
                            .param("bookmarkId", bookmarkId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(dto))
                            .session(session))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            String responseMsg = toResponseMsg(mvcResult);

            assertThat(responseMsg).isEqualTo(new TagException(FAILED_UNTAGGING, BOOKMARK_ID_NOT_FOUND, tagId).getMessage());
        }

        @Test
        @DisplayName("태그 삭제 실패 - 리퀘스트 바디 데이터 잘못 온 경우")
        void untagging_BindingException_Fail() throws Exception {
            //given
            Long tagId = 1L;
            UnTaggingRequestDto dto = new UnTaggingRequestDto();
            dto.setTagId("0");
            doThrow(new TagException(FAILED_UNTAGGING, BOOKMARK_ID_NOT_FOUND, tagId))
                    .when(bookmarkService).untagging(user, dto, bookmarkId);

            //when then
            MvcResult mvcResult = mockMvc.perform(delete("/bookmark/tag")
                            .param("bookmarkId", bookmarkId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(dto))
                            .session(session))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            String responseMsg = toResponseMsg(mvcResult);

            assertThat(responseMsg).isEqualTo(FAILED_UNTAGGING.message());
        }
    }

    private String toResponseMsg(MvcResult mvcResult) throws UnsupportedEncodingException {
        String response = mvcResult.getResponse().getContentAsString();
        return JsonPath.parse(response).read("$.message");
    }

    private <T> String toJson(T obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}