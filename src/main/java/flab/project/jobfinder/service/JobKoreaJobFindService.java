package flab.project.jobfinder.service;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitDto;
import flab.project.jobfinder.dto.RecruitPageDto;
import flab.project.jobfinder.service.crawler.CrawlerService;
import flab.project.jobfinder.service.parser.ParserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class JobKoreaJobFindService implements JobFindService {

    private final CrawlerService jobKoreaCrawlerService;
    private final ParserService jobKoreaParserService;
    private final JobKoreaPropertiesConfig config;

    @Override
    public RecruitPageDto findJobByPage(DetailedSearchDto dto, int page) {
        Document doc = jobKoreaCrawlerService.crawl(dto, page);
        int totalPage = getTotalPage(doc);
        List<RecruitDto> recruitDtoList = parsePage(doc);

        log.info("total page: {}", totalPage);

        return RecruitPageDto.builder()
                .list(recruitDtoList)
                .totalPage(totalPage)
                .build();
    }

    private int getTotalPage(Document doc) {
        String numSelector = config.getNumSelector();
        String pageNumStr = doc.select(numSelector).text()
                            .replaceAll("[^0-9]", "");
        return Integer.parseInt(pageNumStr) / 20 + 1;
    }

    private List<RecruitDto> parsePage(Document pageDoc) {
        Elements recruits = pageDoc.select(config.getSelector());
        return jobKoreaParserService.parse(recruits);
    }
}
