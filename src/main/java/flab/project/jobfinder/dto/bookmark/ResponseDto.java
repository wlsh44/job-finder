package flab.project.jobfinder.dto.bookmark;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private HttpStatus status;
    private String message;
    private T data;
}
