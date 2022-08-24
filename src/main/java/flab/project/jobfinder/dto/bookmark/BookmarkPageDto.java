package flab.project.jobfinder.dto.bookmark;

import flab.project.jobfinder.dto.page.PageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class BookmarkPageDto {
    private List<BookmarkResponseDto> bookmarkList;
    private PageDto pageDto;
}
