package flab.project.jobfinder.dto.page;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageDto {
    private int totalPage;
    private int startPage;
}
