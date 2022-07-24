package flab.project.jobfinder.controller.advice;

import flab.project.jobfinder.dto.bookmark.CategoryResponseDto;
import flab.project.jobfinder.dto.bookmark.ResponseDto;
import flab.project.jobfinder.exception.bookmark.BookmarkException;
import flab.project.jobfinder.exception.bookmark.CategoryException;
import flab.project.jobfinder.exception.bookmark.TagException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static flab.project.jobfinder.enums.bookmark.BookmarkResponseCode.FAILED_CREATE_BOOKMARK;
import static flab.project.jobfinder.enums.bookmark.CategoryResponseCode.FAILED_CREATE_CATEGORY;
import static flab.project.jobfinder.enums.bookmark.CategoryResponseCode.FAILED_GET_CATEGORIES;

@Slf4j
@ResponseBody
@ControllerAdvice
public class BookmarkControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CategoryException.class)
    public ResponseDto<CategoryResponseDto> categoryException(CategoryException e) {
        log.error(e.getMessage());
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BookmarkException.class)
    public ResponseDto<List<CategoryResponseDto>> bookmarkException(BookmarkException e) {
        log.error(e.getMessage());
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseDto<List<CategoryResponseDto>> bindingException(BindException e) {
        log.error(e.getMessage());
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, FAILED_CREATE_CATEGORY.message(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TagException.class)
    public ResponseDto<List<CategoryResponseDto>> tagException(TagException e) {
        log.error(e.getMessage());
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return "/";
    }
}
