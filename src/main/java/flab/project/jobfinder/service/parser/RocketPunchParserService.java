package flab.project.jobfinder.service.parser;

import flab.project.jobfinder.config.rocketpunch.RocketPunchPropertiesConfig;
import flab.project.jobfinder.dto.RecruitDto;
import flab.project.jobfinder.service.parser.duedate.DueDateParser;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static flab.project.jobfinder.enums.Platform.ROCKETPUNCH;

@Component
@RequiredArgsConstructor
public class RocketPunchParserService implements ParserService {

    private final RocketPunchPropertiesConfig config;
    private final DueDateParser dueDateParser;

    @Override
    public List<RecruitDto> parse(Elements recruits) {
        List<RecruitDto> recruitDtoList = new ArrayList<>();

        for (Element recruit : recruits) {
            String corp = parseCorp(recruit);
            String metaTech = parseTechStack(recruit);
            Elements recruitDetails = recruit.select("div.company-jobs-detail > div.job-detail");

            for (Element recruitDetail : recruitDetails) {
                String title = parseTitle(recruitDetail);
                String url = parseUrl(recruitDetail);
                String career = parseCareer(recruitDetail);
                boolean alwaysRecruit = parseAlwaysRecruit(recruitDetail);
                LocalDate dueDate = parseDueDate(recruitDetail, alwaysRecruit);

                RecruitDto recruitDto = getRecruitDto(corp, metaTech, title, url, career, dueDate, alwaysRecruit);
                recruitDtoList.add(recruitDto);
            }
        }
        return recruitDtoList;
    }

    private RecruitDto getRecruitDto(String corp, String metaTech, String title, String url, String career, LocalDate dueDate, boolean alwaysRecruit) {
        return RecruitDto.builder()
                .corp(corp)
                .title(title)
                .techStack(metaTech)
                .url(url)
                .career(career)
                .dueDate(dueDate)
                .alwaysRecruit(alwaysRecruit)
                .location("")
                .jobType("")
                .platform(ROCKETPUNCH.koreaName())
                .build();
    }

    private String parseTechStack(Element recruit) {
        Elements meta = recruit.select("div.meta");
        return meta.text();
    }

    private boolean parseAlwaysRecruit(Element recruitDetail) {
        Element dueDate = recruitDetail.select("div.job-dates > span").first();
        if (dueDate == null) {
            return false;
        }
        return dueDateParser.isAlwaysRecruiting(dueDate.text(), config.getAlwaysRecruitFormat());
    }

    //기간, 원격 유뮤, 등록 날짜 3개 순으로 되어 있으므로 첫 번째 span 값 가져옴
    private LocalDate parseDueDate(Element recruitDetail, boolean alwaysRecruit) {
        Element dueDate = recruitDetail.select("div.job-dates > span").first();
        if (dueDate == null || alwaysRecruit) {
            return null;
        }
        return dueDateParser.parseDueDate(dueDate.text(), config.getParseFormat());
    }

    private String parseCareer(Element recruitDetail) {
        String info = recruitDetail.select("span.job-stat-info").text();
        String[] infoArr = info.split("/");
        return infoArr[infoArr.length - 1];
    }

    private String parseUrl(Element recruitDetail) {
        return config.getUrl() + recruitDetail.select("a.job-title")
                                                .attr("href");
    }

    private String parseCorp(Element recruit) {
        Elements companyName = recruit.select("h4.name > strong");
        return companyName.text();
    }

    private String parseTitle(Element recruitDetail) {
        Elements companyName = recruitDetail.select("a.job-title");
        return companyName.text();
    }
}
