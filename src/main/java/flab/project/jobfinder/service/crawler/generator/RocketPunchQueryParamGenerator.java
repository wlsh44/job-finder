package flab.project.jobfinder.service.crawler.generator;

import flab.project.jobfinder.config.RocketPunchPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.enums.JobType;
import flab.project.jobfinder.enums.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static flab.project.jobfinder.enums.CareerType.ANY;

@RequiredArgsConstructor
public class RocketPunchQueryParamGenerator implements QueryParamGenerator {

    public static final int PAY_UNIT = 10000;
    public static final String JOB_TYPE_KEY = "hiring_types";
    public static final String LOCATION_KEY = "location";
    public static final String PAY_KEY = "salary";
    public static final String PAGE_KEY = "page";
    public static final String SEARCH_TEXT_KEY = "keywords";
    public static final String CAREER_TYPE_KEY = "career_type";

    private final RocketPunchPropertiesConfig config;

    @Override
    public MultiValueMap<String, String> toQueryParams(DetailedSearchDto dto, int pageNum) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        Optional.ofNullable(dto.getSearchText())
                .ifPresent(searchText -> map.add(SEARCH_TEXT_KEY, searchText));
        Optional.ofNullable(dto.getLocation())
                .ifPresent(locations -> map.add(LOCATION_KEY, toLocationParam(locations)));
        Optional.ofNullable(dto.getCareer())
                .filter(career -> !ANY.equals(career.getCareerType()))
                .ifPresent(career -> map.add(CAREER_TYPE_KEY, toCareerParam(career)));
        Optional.ofNullable(dto.getJobType())
                .ifPresent(jobTypes -> map.add(JOB_TYPE_KEY, toJobTypeParam(jobTypes)));
        Optional.ofNullable(dto.getPay())
                .filter(pay -> !(pay.getPayMin() == null && pay.getPayMax() == null))
                .ifPresent(pay -> map.add(PAY_KEY, toPayParam(pay)));
        map.add(PAGE_KEY, String.valueOf(pageNum));

        return map;
    }

    private String toJobTypeParam(List<JobType> jobTypes) {
        return jobTypes.stream()
                .map(JobType::rocketPunchCode)
                .collect(Collectors.joining(config.getDelimiter() + JOB_TYPE_KEY + "="));
    }

    private String toLocationParam(List<Location> locations) {
        return locations.stream()
                .map(Location::rocketPunchCode)
                .collect(Collectors.joining(config.getDelimiter() + LOCATION_KEY + "="));
    }

    private String toCareerParam(DetailedSearchDto.Career career) {
        return career.getCareerType().rocketPunchCode();
    }

    private String toPayParam(DetailedSearchDto.Pay pay) {
        StringBuilder params = new StringBuilder();

        Integer payMin = Optional.ofNullable(pay.getPayMin()).orElse(0);
        Integer payMax = pay.getPayMax();
        params.append(payMin * PAY_UNIT).append("-");
        if (payMax != null) {
            params.append(payMax);
        }
        return params.toString();
    }
}
