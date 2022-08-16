package flab.project.jobfinder.controller.advice;

import flab.project.jobfinder.dto.bookmark.*;
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
import static flab.project.jobfinder.enums.bookmark.TagResponseCode.FAILED_TAGGING;
import static flab.project.jobfinder.enums.bookmark.TagResponseCode.FAILED_UNTAGGING;

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
    public ResponseDto<Object> bindingException(BindException e) {
        Object target = e.getBindingResult().getTarget();

        log.error(e.getMessage());
        if (target instanceof NewCategoryRequestDto){
            return new ResponseDto<>(HttpStatus.BAD_REQUEST, FAILED_CREATE_CATEGORY.message(), null);
        } else if (target instanceof NewBookmarkRequestDto) {
            return new ResponseDto<>(HttpStatus.BAD_REQUEST, FAILED_CREATE_BOOKMARK.message(), null);
        } else if (target instanceof TaggingRequestDto) {
            return new ResponseDto<>(HttpStatus.BAD_REQUEST, FAILED_TAGGING.message(), null);
        } else if (target instanceof UnTaggingRequestDto) {
            return new ResponseDto<>(HttpStatus.BAD_REQUEST, FAILED_UNTAGGING.message(), null);
        } else {
            return new ResponseDto<>(HttpStatus.BAD_REQUEST, null, null);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TagException.class)
    public ResponseDto<List<CategoryResponseDto>> tagException(TagException e) {
        log.error(e.getMessage());
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    public String nullPointException(NullPointerException e) {
        log.error(e.getMessage());
        return "/";
    }
}
