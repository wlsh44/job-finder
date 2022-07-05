package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.service.user.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static flab.project.jobfinder.consts.SessionConst.LOGIN_SESSION_ID;
import static flab.project.jobfinder.enums.bookmark.BookmarkResponseCode.*;
import static flab.project.jobfinder.enums.bookmark.CategoryResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/")
    public ResponseDto<CategoryResponseDto> createBookmark(
            @SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
            NewCategoryRequestDto requestDto) {
        CategoryResponseDto categoryResponseDto = bookmarkService.createCategory(user, requestDto);
        return new ResponseDto<>(HttpStatus.OK, CREATE_CATEGORY.message(), categoryResponseDto);
    }

    @GetMapping("/category")
    public ResponseDto<List<CategoryResponseDto>> categoryList(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user) {
        List<CategoryResponseDto> responseDtoList = bookmarkService.findCategoriesByUser(user);
        return new ResponseDto<>(HttpStatus.OK, GET_CATEGORIES.message(), responseDtoList);
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseDto<CategoryResponseDto> deleteCategory(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                                    @PathVariable Long categoryId) {
        CategoryResponseDto categoryResponseDto = bookmarkService.deleteCategory(user, categoryId);
        return new ResponseDto<>(HttpStatus.OK, DELETE_CATEGORY.message(), categoryResponseDto);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseDto<List<BookmarkResponseDto>> bookmarkList(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                                               @PathVariable Long categoryId) {
        List<BookmarkResponseDto> responseDtoList = bookmarkService.findAllBookmarksByCategory(user, categoryId);
        return new ResponseDto<>(HttpStatus.OK, GET_BOOKMARKS.message(), responseDtoList);
    }

    @PostMapping("/category/{categoryId}")
    public ResponseDto<BookmarkResponseDto> bookmarkRecruit(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                                            @PathVariable Long categoryId, NewBookmarkRequestDto dto) {
        BookmarkResponseDto responseDto = bookmarkService.bookmarkRecruit(user, categoryId, dto);
        return new ResponseDto<>(HttpStatus.OK, CREATE_BOOKMARK.message(), responseDto);
    }

    @DeleteMapping("/category/{categoryId}/{bookmarkId}")
    public ResponseDto<BookmarkResponseDto> unbookmark(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                                       @PathVariable Long categoryId, @PathVariable Long bookmarkId) {
        BookmarkResponseDto responseDto = bookmarkService.unbookmarkRecruit(user, categoryId, bookmarkId);
        return new ResponseDto<>(HttpStatus.OK, DELETE_BOOKMARK.message(), responseDto);
    }
}
