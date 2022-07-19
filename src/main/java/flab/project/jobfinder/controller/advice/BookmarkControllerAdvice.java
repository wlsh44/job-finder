package flab.project.jobfinder.controller.advice;

import flab.project.jobfinder.dto.bookmark.CategoryResponseDto;
import flab.project.jobfinder.dto.bookmark.ResponseDto;
import flab.project.jobfinder.exception.bookmark.FindCategoryFailedException;
import flab.project.jobfinder.exception.bookmark.CreateBookmarkFailedException;
import flab.project.jobfinder.exception.bookmark.CreateCategoryFailedException;
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
    @ExceptionHandler(CreateCategoryFailedException.class)
    public ResponseDto<CategoryResponseDto> createCategoryFailedException(CreateCategoryFailedException e) {
        log.info(e.getMessage());
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, FAILED_CREATE_CATEGORY.message(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FindCategoryFailedException.class)
    public ResponseDto<List<CategoryResponseDto>> categoryNotFoundException(FindCategoryFailedException e) {
        log.info(e.getMessage());
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, FAILED_GET_CATEGORIES.message(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseDto<List<CategoryResponseDto>> bindingException(BindException e) {
        log.info(e.getMessage());
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, FAILED_CREATE_CATEGORY.message(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CreateBookmarkFailedException.class)
    public ResponseDto<List<CategoryResponseDto>> createBookmarkFailedException(CreateBookmarkFailedException e) {
        log.info(e.getMessage());
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, FAILED_CREATE_BOOKMARK.message(), null);
    }
}
