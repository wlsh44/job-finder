package flab.project.jobfinder.service;

import flab.project.jobfinder.config.jobkorea.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.PageDto;
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
public class JobKoreaJobFindService implements JobFindService {

    private final CrawlerService jobKoreaCrawlerService;
    private final ParserService jobKoreaParserService;
    private final PaginationParser jobKoreaPaginationParser;
    private final JobKoreaPropertiesConfig config;

    @Override
    public RecruitPageDto findJobByPage(DetailedSearchDto dto, int page) throws CrawlFailedException {
        Document doc = jobKoreaCrawlerService.crawl(dto, page);
        int totalPage = jobKoreaPaginationParser.getTotalPage(doc, config);
        int startPage = jobKoreaPaginationParser.getStartPage(page);
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
        return jobKoreaParserService.parse(recruits);
    }

    @Override
    public Platform getPlatform() {
        return Platform.JOBKOREA;
    }
}
