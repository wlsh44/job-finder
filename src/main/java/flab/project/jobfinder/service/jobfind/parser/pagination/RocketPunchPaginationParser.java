package flab.project.jobfinder.service.jobfind.parser.pagination;

import flab.project.jobfinder.config.JobFinderPropertiesConfig;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class RocketPunchPaginationParser extends PaginationParser {

    @Override
    public int getTotalPage(Document doc, JobFinderPropertiesConfig config) {
        String totalPageSelector = config.getPageSelector();

        Elements select = doc.select(totalPageSelector);
        if (select.size() == 0) {
            return 1;
        }
        String totalPageStr = select.last().text();
        return Integer.parseInt(totalPageStr);
    }
}
