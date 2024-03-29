package flab.project.jobfinder.controller;

import flab.project.jobfinder.dto.bookmark.CategoryResponseDto;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.service.user.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

import static flab.project.jobfinder.consts.SessionConst.LOGIN_SESSION_ID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final CategoryService categoryService;

    @GetMapping("/my-page")
    public String myPage(Model model) {

        return "user/my-page";
    }

    @GetMapping("/my-bookmark")
    public String bookmark(@SessionAttribute(name = LOGIN_SESSION_ID, required = false) User user, Model model) {
        List<CategoryResponseDto> userCategoryList = categoryService.findAllByUser(user);
        model.addAttribute("categories", userCategoryList);
        return "bookmark";
    }
}
