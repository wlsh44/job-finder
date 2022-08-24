package flab.project.jobfinder.service.jobfind.parser.pagination;

import flab.project.jobfinder.config.JobFinderPropertiesConfig;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class JobKoreaPaginationParser extends PaginationParser {

    private final static int RECRUIT_COUNT_PER_PAGE = 20;

    @Override
    public int getTotalPage(Document doc, JobFinderPropertiesConfig config) {
        int pageNum = getPageNum(doc, config);
        //page가 1부터 시작하므로 1 더해줌
        return pageNum / RECRUIT_COUNT_PER_PAGE + 1;
    }

    private int getPageNum(Document doc, JobFinderPropertiesConfig config) {
        String numSelector = config.getPageSelector();
        String pageNumStr = doc.select(numSelector)
                .text()
                .replaceAll("[^0-9]", "");
        return Integer.parseInt(pageNumStr);
    }
}
