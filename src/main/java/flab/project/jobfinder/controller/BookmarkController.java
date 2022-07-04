package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.service.user.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static flab.project.jobfinder.consts.SessionConst.LOGIN_SESSION_ID;
import static flab.project.jobfinder.enums.bookmark.CategoryResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/category")
    public ResponseDto<CategoryResponseDto> createBookmark(
            @SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
            NewCategoryRequestDto requestDto) {
        CategoryResponseDto categoryResponseDto = bookmarkService.createCategory(user, requestDto);
        return new ResponseDto<>(HttpStatus.OK, CREATE_CATEGORY.message(), categoryResponseDto);
    }

    @GetMapping("/category")
    public ResponseDto<List<CategoryResponseDto>> categoryList(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user) {
        List<CategoryResponseDto> categoryResponseDtoList = bookmarkService.findCategoriesByUser(user);
        return new ResponseDto<>(HttpStatus.OK, GET_CATEGORIES.message(), categoryResponseDtoList);
    }

    @DeleteMapping("/category")
    public ResponseDto<CategoryResponseDto> deleteCategory(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
                                                    DeleteCategoryRequestDto requestDto) {
        CategoryResponseDto categoryResponseDto = bookmarkService.deleteCategory(user, requestDto);
        return new ResponseDto<>(HttpStatus.OK, DELETE_CATEGORY.message(), categoryResponseDto);
    }
}
