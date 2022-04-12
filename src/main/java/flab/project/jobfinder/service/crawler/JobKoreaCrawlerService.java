package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.dto.DetailedSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static flab.project.jobfinder.util.jobkorea.JobKoreaConst.JOBKOREA_URL;
import static flab.project.jobfinder.util.jobkorea.JobKoreaCrawlerServiceUtil.*;

@Slf4j
@Service
public class JobKoreaCrawlerService implements CrawlerService {

    @Override
    public Document crawling(DetailedSearchDto dto) {
        String url = JOBKOREA_URL + dto.toQueryParams();

        try {
            Document doc = Jsoup.connect(url).get();

            return doc;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
