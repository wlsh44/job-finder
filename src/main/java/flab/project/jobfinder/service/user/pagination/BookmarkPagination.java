package flab.project.jobfinder.service.user.pagination;

import flab.project.jobfinder.dto.page.PageDto;
import flab.project.jobfinder.entity.recruit.Recruit;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class BookmarkPagination {

    private final static int MIDDLE_OF_PAGES = 4;
    private final static int FIRST_PAGE = 1;

    public PageDto toPageDto(Page<Recruit> page) {
        if (page == null) {
            throw new IllegalArgumentException();
        }
        return PageDto.builder()
                .totalPage(getTotalPage(page.getTotalPages()))
                .startPage(getStartPage(page.getNumber()))
                .build();
    }

    private int getTotalPage(int totalPage) {
        if (totalPage == 0) {
            totalPage = FIRST_PAGE;
        }
        return totalPage;
    }

    private int getStartPage(int currentPage) {
        //1, 2, 3, 4 페이지일 때는 startPage = 1
        if (currentPage <= MIDDLE_OF_PAGES) {
            return FIRST_PAGE;
        }
        return currentPage - MIDDLE_OF_PAGES;
    }
}
