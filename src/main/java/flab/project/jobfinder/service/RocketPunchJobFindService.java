package flab.project.jobfinder.service;

import flab.project.jobfinder.config.RocketPunchPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitDto;
import flab.project.jobfinder.dto.RecruitPageDto;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.exception.CrawlFailedException;
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
public class RocketPunchJobFindService implements JobFindService {

    private final CrawlerService rocketPunchCrawlerService;
    private final ParserService rocketPunchParserService;
    private final RocketPunchPropertiesConfig config;

    private final static int MIDDLE_OF_PAGES = 4;
    private final static int FIRST_PAGE = 1;

    @Override
    public RecruitPageDto findJobByPage(DetailedSearchDto dto, int page) throws CrawlFailedException {
        Document doc = rocketPunchCrawlerService.crawl(dto, page);
        int totalPage = getTotalPage(doc);
        List<RecruitDto> recruitDtoList = parsePage(doc);

        log.info("total page: {}", totalPage);

        return RecruitPageDto.builder()
                .recruitDtoList(recruitDtoList)
                .totalPage(totalPage)
                .startPage(getStartPage(page))
                .build();
    }

    private List<RecruitDto> parsePage(Document pageDoc) {
        Elements recruits = pageDoc.select(config.getSelector());
        return rocketPunchParserService.parse(recruits);
    }

    @Override
    public Platform getPlatform() {
        return Platform.ROCKETPUNCH;
    }

    private int getStartPage(int currentPage) {
        //1, 2, 3, 4 페이지일 때는 startPage = 1
        if (currentPage <= MIDDLE_OF_PAGES) {
            return FIRST_PAGE;
        }
        return currentPage - MIDDLE_OF_PAGES;
    }

    private int getTotalPage(Document doc) {
        String totalPageSelector = config.getTotalPageSelector();

        Elements select = doc.select(totalPageSelector);
        if (select.size() == 0) {
            return 1;
        }
        String totalPageStr = select.last().text();
        return Integer.parseInt(totalPageStr);
    }
}
