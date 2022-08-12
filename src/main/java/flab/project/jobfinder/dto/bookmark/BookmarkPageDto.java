package flab.project.jobfinder.dto.bookmark;

import flab.project.jobfinder.dto.page.PageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BookmarkPageDto {
    List<BookmarkResponseDto> bookmarkList;
    PageDto pageDto;
}