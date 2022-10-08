package flab.project.jobfinder.service.jobfind.parser;

import flab.project.jobfinder.config.jobkorea.JobKoreaPropertiesConfig;
import flab.project.jobfinder.service.jobfind.parser.duedate.DueDateParser;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static flab.project.jobfinder.enums.Platform.JOBKOREA;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobKoreaParserService implements ParserService {

    private final JobKoreaPropertiesConfig config;
    private final DueDateParser dueDateParser;

    @Override
    public List<RecruitDto> parse(Elements recruits) {
        List<RecruitDto> recruitDtoList = new ArrayList<>();

        for (Element recruit : recruits) {
            try {
                Elements corpElement = recruit.select("div > div.post-list-corp > a");
                Elements infoElement = recruit.select("div > div.post-list-info");
                Elements optionElement = infoElement.select("p.option");
                Elements etcElement = infoElement.select("p.etc");

                RecruitDto recruitDto = getParseDto(corpElement, infoElement, optionElement, etcElement);
                recruitDtoList.add(recruitDto);
            } catch (Exception e) {
                log.error("잡코리아 파싱 실패: {}", e.getMessage());
            }
        }
        return recruitDtoList;
    }

    private RecruitDto getParseDto(Elements corpElement, Elements infoElement, Elements optionElement, Elements etcElement) throws Exception {
        boolean alwaysRecruit = parseAlwaysRecruit(optionElement);

        RecruitDto recruitDto = RecruitDto.builder()
                .title(parseTitle(infoElement))
                .corp(parseCorp(corpElement))
                .url(parseUrl(corpElement))
                .career(parseCareer(optionElement))
                .location(parseLocation(optionElement))
                .isAlwaysRecruiting(alwaysRecruit)
                .dueDate(parseDueDate(optionElement, alwaysRecruit))
                .jobType(parseJobType(optionElement))
                .techStack(parseTechStack(etcElement))
                .platform(JOBKOREA.koreaName())
                .build();
        return recruitDto;
    }

    private String parseTechStack(Elements etcElement) throws Exception {
        if (etcElement == null) {
            return "";
        }
        return etcElement.text();
    }

    private boolean parseAlwaysRecruit(Elements optionElement) throws Exception {
        if (optionElement == null) {
            return false;
        }
        String dueDateStr = optionElement.select("span.date").text();
        return dueDateParser.isAlwaysRecruiting(dueDateStr, config.getAlwaysRecruitingFormat());
    }

    private LocalDate parseDueDate(Elements optionElement, boolean alwaysRecruit) throws Exception {
        if (optionElement == null || alwaysRecruit) {
            return null;
        }
        String dueDateStr = optionElement.select("span.date").text();
        return dueDateParser.parseDueDate(dueDateStr, config.getDueDateFormat());
    }

    private String parseLocation(Elements optionElement) throws Exception {
        if (optionElement == null) {
            return "";
        }
        return optionElement.select("span.long").text();
    }

    private String parseJobType(Elements optionElement) throws Exception {
        if (optionElement == null) {
            return "";
        }
        return optionElement.select("span").get(2).text();
    }

    private String parseCareer(Elements optionElement) throws Exception {
        if (optionElement == null) {
            return "";
        }
        return optionElement.select("span.exp").text().replace("[^0-9]", "");
    }

    private String parseUrl(Elements corpElement) throws Exception {
        return config.getUrl() + corpElement.attr("href");
    }

    private String parseCorp(Elements corpElement) throws Exception {
        return corpElement.attr("title");
    }

    private String parseTitle(Elements infoElement) throws Exception {
        return infoElement.select("a").attr("title");
    }
}
