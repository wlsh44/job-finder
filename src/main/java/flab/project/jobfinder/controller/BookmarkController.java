package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.bookmark.BookmarkRequestDto;
import flab.project.jobfinder.dto.bookmark.BookmarkResponseDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.service.user.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import static flab.project.jobfinder.consts.SessionConst.LOGIN_SESSION_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/create")
    public BookmarkResponseDto createBookmark(
            @SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user,
            BookmarkRequestDto requestDto) {
        Category newCategory = bookmarkService.createCategory(user, requestDto);

    }
}
