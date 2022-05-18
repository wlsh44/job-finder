package flab.project.jobfinder.service;

import flab.project.jobfinder.config.RocketPunchPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.RecruitDto;
import flab.project.jobfinder.dto.RecruitPageDto;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.exception.CrawlFailedException;
import flab.project.jobfinder.service.crawler.CrawlerService;
import flab.project.jobfinder.service.parser.ParserService;
import flab.project.jobfinder.service.parser.pagination.PaginationParser;
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
    private final PaginationParser rocketPunchPaginationParser;
    private final RocketPunchPropertiesConfig config;

    @Override
    public RecruitPageDto findJobByPage(DetailedSearchDto dto, int page) throws CrawlFailedException {
        Document doc = rocketPunchCrawlerService.crawl(dto, page);
        int totalPage = rocketPunchPaginationParser.getTotalPage(doc, config);
        int startPage = rocketPunchPaginationParser.getStartPage(page);
        List<RecruitDto> recruitDtoList = parsePage(doc);

        log.info("total page: {}", totalPage);

        return RecruitPageDto.builder()
                .recruitDtoList(recruitDtoList)
                .totalPage(totalPage)
                .startPage(startPage)
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
}
