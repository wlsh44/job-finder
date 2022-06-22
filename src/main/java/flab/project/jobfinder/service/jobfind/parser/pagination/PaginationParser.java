package flab.project.jobfinder.service.jobfind.parser.pagination;

import flab.project.jobfinder.config.JobFinderPropertiesConfig;
import org.jsoup.nodes.Document;

public abstract class PaginationParser {

    private final static int MIDDLE_OF_PAGES = 4;
    private final static int FIRST_PAGE = 1;

    public abstract int getTotalPage(Document doc, JobFinderPropertiesConfig config);

    public int getStartPage(int currentPage) {
        //1, 2, 3, 4 페이지일 때는 startPage = 1
        if (currentPage <= MIDDLE_OF_PAGES) {
            return FIRST_PAGE;
        }
        return currentPage - MIDDLE_OF_PAGES;
    }
}
