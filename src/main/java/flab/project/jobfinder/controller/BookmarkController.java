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

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/category")
    public String createBookmark(
            @SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
            @Valid NewCategoryRequestDto requestDto, Model model) {
        log.info("createBookmark");
        log.info("category name: {}", requestDto.getName());
        List<CategoryDto> categoryList = bookmarkService.createCategory(user, requestDto);
        model.addAttribute("categoryList", categoryList);
        return "job-find/recruits :: categoryList";
    }

    @GetMapping("/category")
    public String modalCategoryList(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                    Model model) {
        List<CategoryDto> categoryList = bookmarkService.findCategoriesByUser(user);
        log.info("categoryList");
        model.addAttribute("categoryList", categoryList);
        return "job-find/recruits :: categoryList";
    }

    @GetMapping("/my-page/bookmark")
    public String myPageCategoryList(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                     Model model) {
        List<CategoryDto> categoryList = bookmarkService.findCategoriesByUser(user);
        log.info("categoryList");
        model.addAttribute("categoryList", categoryList);
        return "/user/category-list";
    }

    @DeleteMapping("/my-page/bookmark/")
    public String deleteCategory(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                 @RequestParam Long categoryId, Model model) {
        List<CategoryDto> categoryList = bookmarkService.deleteCategory(user, categoryId);
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
                                                            @RequestBody NewBookmarkRequestDto2 dto) {
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

//    @GetMapping("")
//    public ResponseDto<List<CategoryResponseDto>> categoryList(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user) {
//        List<CategoryResponseDto> responseDtoList = bookmarkService.findCategoriesByUser(user);
//        log.info("categoryList");
//        return new ResponseDto<>(HttpStatus.OK, GET_CATEGORIES.message(), responseDtoList);
//    }

//
//    @GetMapping("/{categoryId}")
//    public ResponseDto<List<BookmarkResponseDto>> bookmarkList(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
//                                                               @PathVariable Long categoryId) {
//        List<BookmarkResponseDto> responseDtoList = bookmarkService.findAllBookmarksByCategory(user, categoryId);
//        return new ResponseDto<>(HttpStatus.OK, GET_BOOKMARKS.message(), responseDtoList);
//    }

}
