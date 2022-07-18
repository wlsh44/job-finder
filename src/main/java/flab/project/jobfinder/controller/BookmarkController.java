package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.service.user.BookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static flab.project.jobfinder.consts.SessionConst.LOGIN_SESSION_ID;
import static flab.project.jobfinder.enums.bookmark.BookmarkResponseCode.*;
import static flab.project.jobfinder.enums.bookmark.TagResponseCode.ADD_TAG;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/category")
    public String createCategory(
            @SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
            @Valid NewCategoryRequestDto requestDto, Model model) {
        log.info("createBookmark");
        log.info("category name: {}", requestDto.getName());
        List<CategoryResponseDto> categoryList = bookmarkService.createCategory(user, requestDto);
        model.addAttribute("categoryList", categoryList);
        return "job-find/recruits :: categoryList";
    }

    @GetMapping("/category")
    public String modalCategoryList(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                    Model model) {
        List<CategoryResponseDto> categoryList = bookmarkService.findCategoriesByUser(user);
        log.info("categoryList");
        model.addAttribute("categoryList", categoryList);
        return "job-find/recruits :: categoryList";
    }

    @GetMapping("/my-page/bookmark")
    public String myPageCategoryList(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                     Model model) {
        List<CategoryResponseDto> categoryList = bookmarkService.findCategoriesByUser(user);
        log.info("categoryList");
        model.addAttribute("categoryList", categoryList);
        return "/user/category-list";
    }

    @DeleteMapping("/my-page/bookmark/")
    public String deleteCategory(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                 @RequestParam Long categoryId, Model model) {
        List<CategoryResponseDto> categoryList = bookmarkService.deleteCategory(user, categoryId);
        model.addAttribute("categoryList", categoryList);
        return "user/category-list :: categoryList";
    }

    @GetMapping("/my-page/bookmark/{categoryId}")
    public String bookmarkList(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                               @PathVariable Long categoryId, Model model) {
        List<BookmarkResponseDto> bookmarkList = bookmarkService.findAllBookmarksByCategory(user, categoryId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("bookmarkList", bookmarkList);
        return "/user/bookmark-list";
    }

    @ResponseBody
    @PostMapping("/bookmark")
    public ResponseDto<List<BookmarkResponseDto>> bookmarkRecruit(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                                            @RequestBody NewBookmarkRequestDto dto) {
        log.info("bookmark");
        log.info(dto.toString());
        List<BookmarkResponseDto> responseDto = bookmarkService.bookmarkRecruit(user, dto);
        return new ResponseDto<>(HttpStatus.OK, CREATE_BOOKMARK.message(), responseDto);
    }

    @DeleteMapping("/my-page/bookmark/{categoryId}")
    public String unbookmark(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                             @PathVariable Long categoryId, @RequestParam Long bookmarkId,
                             Model model) {
        List<BookmarkResponseDto> bookmarkList = bookmarkService.unbookmarkRecruit(user, categoryId, bookmarkId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("bookmarkList", bookmarkList);
        return "user/bookmark-list :: bookmarkList";
    }

    @PostMapping("/tag")
    public String createTag(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                            @Valid NewTagRequestDto dto, Model model) {
        List<TagDto> responseDtoList = bookmarkService.createTag(user, dto);
        model.addAttribute("tagList", responseDtoList);
        return "user/bookmark-list :: tagList";
    }

    @ResponseBody
    @PutMapping("/tag")
    public ResponseDto<List<TagDto>> tagging(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                          @RequestBody @Valid TaggingRequestDto dto, @RequestParam Long bookmarkId) {
        log.info(dto.toString());
        List<TagDto> tagList = bookmarkService.tagging(user, bookmarkId, dto);
        return new ResponseDto<>(HttpStatus.OK, ADD_TAG.message(), tagList);
    }

    @GetMapping("/tag")
    public String getBookmarkTags(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                  Model model) {
        List<TagDto> tagList = bookmarkService.findTagByUser(user);
        model.addAttribute("tagList", tagList);
        return "user/bookmark-list :: tagList";
    }
}
