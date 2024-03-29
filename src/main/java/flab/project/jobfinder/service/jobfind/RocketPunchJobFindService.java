package flab.project.jobfinder.service.jobfind;

import flab.project.jobfinder.config.rocketpunch.RocketPunchPropertiesConfig;
import flab.project.jobfinder.dto.form.DetailedSearchDto;
import flab.project.jobfinder.dto.page.PageDto;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.dto.page.RecruitPageDto;
import flab.project.jobfinder.enums.Platform;
import flab.project.jobfinder.exception.CrawlFailedException;
import flab.project.jobfinder.service.jobfind.crawler.CrawlerService;
import flab.project.jobfinder.service.jobfind.parser.ParserService;
import flab.project.jobfinder.service.jobfind.parser.pagination.PaginationParser;
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

        PageDto pageDto = PageDto.builder()
                .startPage(startPage)
                .totalPage(totalPage)
                .build();

        log.info("total page: {}", totalPage);

        return RecruitPageDto.builder()
                .recruitDtoList(recruitDtoList)
                .pageDto(pageDto)
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
