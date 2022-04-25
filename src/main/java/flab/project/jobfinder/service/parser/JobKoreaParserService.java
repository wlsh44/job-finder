package flab.project.jobfinder.service.parser;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.ParseDto;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobKoreaParserService implements ParserService {

    private final JobKoreaPropertiesConfig config;

    @Override
    public List<ParseDto> parse(Elements recruits) {
        List<ParseDto> parseDtoList = new ArrayList<>();

        for (Element recruit : recruits) {
            Elements corpElement = recruit.select("div > div.post-list-corp > a");
            Elements infoElement = recruit.select("div > div.post-list-info");
            Elements optionElement = infoElement.select("p.option");
            Elements etcElement = infoElement.select("p.etc");

            ParseDto parseDto = ParseDto.builder()
                    .title(parseTitle(infoElement))
                    .corp(parseCorp(corpElement))
                    .url(parseUrl(corpElement))
                    .career(parseCareer(optionElement))
                    .location(parseLocation(optionElement))
                    .date(parseDate(optionElement))
                    .jobType(parseJobType(optionElement))
                    .techStack(parseTechStack(etcElement))
                    .platform(config.getPlatform())
                    .build();
            parseDtoList.add(parseDto);
        }
        return parseDtoList;
    }

    private String parseTechStack(Elements etcElement) {
        return etcElement.text();
    }

    private String parseDate(Elements optionElement) {
        return optionElement.select("span.date").text();
    }

    private String parseLocation(Elements optionElement) {
        return optionElement.select("span.long").text();
    }

    private String parseJobType(Elements optionElement) {
        return optionElement.select("span").get(2).text();
    }

    private String parseCareer(Elements optionElement) {
        return optionElement.select("span.exp").text().replace("[^0-9]", "");
    }

    private String parseUrl(Elements corpElement) {
        return config.getUrl() + corpElement.attr("href");
    }

    private String parseCorp(Elements corpElement) {
        return corpElement.attr("title");
    }

    private String parseTitle(Elements infoElement) {
        return infoElement.select("a").attr("title");
    }
}
