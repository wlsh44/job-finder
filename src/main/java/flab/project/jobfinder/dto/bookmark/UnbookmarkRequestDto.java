package flab.project.jobfinder.dto.bookmark;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnbookmarkRequestDto {
    private Long bookmarkId;
    private String categoryName;
}
