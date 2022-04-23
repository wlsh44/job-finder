package flab.project.jobfinder.service.perser;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.dto.ParseDto;
import flab.project.jobfinder.service.crawler.CrawlerService;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class JobKoreaParserService implements ParserService {

    private final CrawlerService jobKoreaCrawlerService;
    private final JobKoreaPropertiesConfig config;

    @Override
    public List<ParseDto> parse(DetailedSearchDto dto) {
        int pageNum = 1;
        List<ParseDto> parseDtoList = new ArrayList<>();
        Elements recruits;

        do {
            Document doc = jobKoreaCrawlerService.crawl(dto, pageNum++);
            recruits = doc.select(config.getSelector());

            for (Element recruit : recruits) {
                Elements corpElement = recruit.select("div > div.post-list-corp > a");
                Elements infoElement = recruit.select("div > div.post-list-info");
                Elements optionElement = infoElement.select("p.option");
                Elements etcElement = infoElement.select("p.etc");

                String title = infoElement.select("a").attr("title");
                String corp = corpElement.attr("title");
                String url = config.getUrl() + corpElement.attr("href");
                String career = optionElement.select("span.exp").text().replace("[^0-9]", "");
                String jobType = optionElement.select("span").get(2).text();
                String loc = optionElement.select("span.long").text();
                String date = optionElement.select("span.date").text();
                String stack = etcElement.text();

                ParseDto parseDto = ParseDto.builder()
                        .title(title)
                        .corp(corp)
                        .url(url)
                        .career(career)
                        .location(loc)
                        .date(date)
                        .jobType(jobType)
                        .techStack(stack)
                        .build();
                parseDtoList.add(parseDto);
            }
        } while (recruits.size() > 0);

        return parseDtoList;
    }
}
