package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.dto.page.PageDto;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.service.user.BookmarkService;
import flab.project.jobfinder.service.user.RecruitService;
import flab.project.jobfinder.service.user.CategoryService;
import flab.project.jobfinder.service.user.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static flab.project.jobfinder.consts.SessionConst.LOGIN_SESSION_ID;
import static flab.project.jobfinder.enums.bookmark.BookmarkResponseCode.*;
import static flab.project.jobfinder.enums.bookmark.TagResponseCode.TAGGING;
import static flab.project.jobfinder.enums.bookmark.TagResponseCode.UNTAGGING;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    private static final int PAGE_OFFSET = 1;

    @PostMapping("/category")
    public String createCategory(
            @SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
            @Valid NewCategoryRequestDto requestDto, Model model) {
        List<CategoryResponseDto> categoryList = bookmarkService.createCategory(user, requestDto);
        model.addAttribute("categoryList", categoryList);
        return "job-find/recruits :: categoryList";
    }

    @GetMapping("/category")
    public String modalCategoryList(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                    Model model) {
        List<CategoryResponseDto> categoryList = bookmarkService.findAllCategoryByUser(user);
        model.addAttribute("categoryList", categoryList);
        return "job-find/recruits :: categoryList";
    }

    @GetMapping("/my-page/bookmark")
    public String myPageCategoryList(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                     Model model) {
        List<CategoryResponseDto> categoryList = bookmarkService.findAllCategoryByUser(user);
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
                               @PathVariable Long categoryId, Model model, @RequestParam(required = false) Integer page) {
        if (page == null || page < PAGE_OFFSET) {
            page = PAGE_OFFSET;
        }
        //JPA 페이징 처리를 0부터 하기 때문에 -1 처리
        Pageable pageable = PageRequest.of(page - PAGE_OFFSET, 20);

        BookmarkPageDto bookmarkPageDto = bookmarkService.findAllBookmarkByCategory(user, categoryId, pageable);
        PageDto pageDto = bookmarkPageDto.getPageDto();

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("bookmarkList", bookmarkPageDto.getBookmarkList());
        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", pageDto.getStartPage());
        model.addAttribute("totalPage", pageDto.getTotalPage());
        return "/user/bookmark-list";
    }

    @ResponseBody
    @PostMapping("/bookmark")
    public ResponseDto<List<BookmarkResponseDto>> bookmarkRecruit(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                                            @RequestBody NewBookmarkRequestDto dto) {
        log.info("bookmark");
        log.info(dto.toString());
        List<BookmarkResponseDto> responseDto = bookmarkService.bookmark(user, dto);
        return new ResponseDto<>(HttpStatus.OK, CREATE_BOOKMARK.message(), responseDto);
    }

    @DeleteMapping("/my-page/bookmark/{categoryId}")
    public String unbookmark(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                             @PathVariable Long categoryId, @RequestParam Long bookmarkId,
                             @RequestParam(required = false) Integer page, Model model) {
        if (page == null || page < PAGE_OFFSET) {
            page = PAGE_OFFSET;
        }
        Pageable pageable = PageRequest.of(page - PAGE_OFFSET, 20);

        BookmarkPageDto bookmarkPageDto = bookmarkService.unbookmark(user, categoryId, bookmarkId, pageable);
        PageDto pageDto = bookmarkPageDto.getPageDto();

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("bookmarkList", bookmarkPageDto.getBookmarkList());
        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", pageDto.getStartPage());
        model.addAttribute("totalPage", pageDto.getTotalPage());
        return "user/bookmark-list :: bookmarkList";
    }

    @ResponseBody
    @PostMapping("/tag")
    public ResponseDto<TagDto> tagging(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                          @RequestBody @Valid TaggingRequestDto dto, @RequestParam Long bookmarkId) {
        log.info(dto.toString());
        TagDto tagDto = bookmarkService.tagging(user, dto, bookmarkId);
        return new ResponseDto<>(HttpStatus.OK, TAGGING.message(), tagDto);
    }

    @ResponseBody
    @DeleteMapping("/bookmark/tag")
    public ResponseDto<List<TagDto>> untagging(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                               @RequestBody @Valid UnTagRequestDto dto, @RequestParam Long bookmarkId) {
        bookmarkService.untagging(user, dto, bookmarkId);
        return new ResponseDto<>(HttpStatus.OK, UNTAGGING.message(), null);
    }
}
