package flab.project.jobfinder.service.parser;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.RecruitDto;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class JobKoreaParserService implements ParserService {

    private final JobKoreaPropertiesConfig config;

    @Override
    public List<RecruitDto> parse(Elements recruits) {
        List<RecruitDto> recruitDtoList = new ArrayList<>();

        for (Element recruit : recruits) {
            Elements corpElement = recruit.select("div > div.post-list-corp > a");
            Elements infoElement = recruit.select("div > div.post-list-info");
            Elements optionElement = infoElement.select("p.option");
            Elements etcElement = infoElement.select("p.etc");

            RecruitDto recruitDto = getParseDto(corpElement, infoElement, optionElement, etcElement);
            recruitDtoList.add(recruitDto);
        }
        return recruitDtoList;
    }

    private RecruitDto getParseDto(Elements corpElement, Elements infoElement, Elements optionElement, Elements etcElement) {
        String platform = config.getPlatform();
        RecruitDto recruitDto = RecruitDto.builder()
                .title(parseTitle(infoElement))
                .corp(parseCorp(corpElement))
                .url(parseUrl(corpElement))
                .career(parseCareer(optionElement))
                .location(parseLocation(optionElement))
                .dueDate(parseDueDate(optionElement))
                .jobType(parseJobType(optionElement))
                .techStack(parseTechStack(etcElement))
                .platform(platform)
                .build();
        return recruitDto;
    }

    private String parseTechStack(Elements etcElement) {
        if (isNull(etcElement)) {
            return "";
        }
        return etcElement.text();
    }

    private String parseDueDate(Elements optionElement) {
        if (isNull(optionElement)) {
            return "";
        }
        return optionElement.select("span.date").text();
    }

    private String parseLocation(Elements optionElement) {
        if (isNull(optionElement)) {
            return "";
        }
        return optionElement.select("span.long").text();
    }

    private String parseJobType(Elements optionElement) {
        if (isNull(optionElement)) {
            return "";
        }
        return optionElement.select("span").get(2).text();
    }

    private String parseCareer(Elements optionElement) {
        if (isNull(optionElement)) {
            return "";
        }
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
