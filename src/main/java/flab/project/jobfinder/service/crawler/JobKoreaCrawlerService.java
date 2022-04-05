package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.util.Location;
import org.jsoup.nodes.Document;

import java.util.stream.Collectors;

import static flab.project.jobfinder.util.JobKoreaConst.JOBKOREA_DELIMITER;
import static flab.project.jobfinder.util.JobKoreaConst.JOBKOREA_URL;

public class JobKoreaCrawlerService implements CrawlerService {

    private String detailedSearchQueryParams(DetailedSearchDto dto) {
        StringBuilder queryParams = new StringBuilder("?stext=");
        String location = getLocation(dto);

        queryParams.append(dto.getSearchText()).append(location);
        return queryParams.toString();
    }

    private String getLocation(DetailedSearchDto dto) {
        if (dto.getLocation() != null) {
            String location = dto.getLocation().stream()
                    .map(Location::jobkoreaCode)
                    .collect(Collectors.joining(JOBKOREA_DELIMITER));
            return "&local=" + location;
        } else {
            return "";
        }
    }

    @Override
    public Document crawling(DetailedSearchDto dto) {
        String url = JOBKOREA_URL + detailedSearchQueryParams(dto);

        return null;
    }
}
