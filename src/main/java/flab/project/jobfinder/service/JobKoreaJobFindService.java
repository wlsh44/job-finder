package flab.project.jobfinder.service;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.ParseDto;
import flab.project.jobfinder.service.crawler.CrawlerService;
import flab.project.jobfinder.service.parser.ParserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Slf4j
@Service
@AllArgsConstructor
public class JobKoreaJobFindService implements JobFindService {

    private final CrawlerService jobKoreaCrawlerService;
    private final ParserService jobKoreaParserService;
    private final JobKoreaPropertiesConfig config;

    @Override
    public List<ParseDto> find(DetailedSearchDto dto) {
        Document doc = jobKoreaCrawlerService.crawl(dto, 1);
        String pageNumStr = doc.select(config.getNumSelector()).text().replaceAll("[^0-9]", "");
        int totalPage = Math.min(Integer.parseInt(pageNumStr) / 20 + 1, config.getMaxThreadPool());

        log.info("total Page: {}", totalPage);

        List<ParseDto> parseDtoList = parsePage(doc);
        parseDtoList.addAll(asyncFind(dto, totalPage));
        return parseDtoList;
    }

    private List<ParseDto> parsePage(Document pageDoc) {
        Elements recruits = pageDoc.select(config.getSelector());
        return jobKoreaParserService.parse(recruits);
    }

    private List<ParseDto> asyncFind(DetailedSearchDto dto, int totalPage) {
        List<ParseDto> parseDtoList = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(totalPage);
        try {
            final List<CompletableFuture<List<ParseDto>>> futures = IntStream.range(2, totalPage + 1)
                    .boxed()
                    .map(pageNum -> CompletableFuture.supplyAsync(() -> parsePage(jobKoreaCrawlerService.crawl(dto, pageNum)), executor)
                            .exceptionally(e -> List.of(ParseDto.builder().build())))
                    .toList();

            futures.stream().map(CompletableFuture::join)
                    .forEach(parseDtoList::addAll);
        } finally {
            executor.shutdown();
        }

        return parseDtoList;
    }
}
