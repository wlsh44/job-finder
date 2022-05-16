package flab.project.jobfinder.service.crawler.generator;

import flab.project.jobfinder.config.RocketPunchPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.enums.JobType;
import flab.project.jobfinder.enums.Location;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static flab.project.jobfinder.enums.CareerType.ANY;

@RequiredArgsConstructor
public class RocketPunchQueryParamGenerator implements QueryParamGenerator {

    public static final int PAY_UNIT = 10000;
    public static final int MAX_PAY = 20000;
    public static final String JOB_TYPE_KEY = "hiring_types";
    public static final String LOCATION_KEY = "location";
    public static final String PAY_KEY = "salary";
    public static final String PAGE_KEY = "page";
    public static final String SEARCH_TEXT_KEY = "keywords";
    public static final String CAREER_TYPE_KEY = "career_type";

    private final RocketPunchPropertiesConfig config;

    @Override
    public String toQueryParams(DetailedSearchDto dto, int pageNum) {
        StringBuilder queryParams = new StringBuilder();
        String searchTextParam = Optional.ofNullable(dto.getSearchText()).map(this::toSearchTextParam).orElse("");
        String locationParam = Optional.ofNullable(dto.getLocation()).map(this::toLocationParam).orElse("");
        String careerParam = Optional.ofNullable(dto.getCareer()).map(this::toCareerParam).orElse("");
        String jobParam = Optional.ofNullable(dto.getJobType()).map(this::toJobTypeParam).orElse("");
        String payParam = Optional.ofNullable(dto.getPay()).map(this::toPayParam).orElse("");
        String pageNumParam = toPageNumParam(pageNum);

        queryParams.append(searchTextParam)
                 .append(locationParam)
                 .append(careerParam)
                 .append(jobParam)
                 .append(payParam)
                 .append(pageNumParam);
        return queryParams.toString();
    }

    private String toSearchTextParam(String searchText) {
        searchText = searchText.replaceAll(" ", "%20");
        return SEARCH_TEXT_KEY + "=" + searchText;
    }

    private String toJobTypeParam(List<JobType> jobTypes) {
        if (jobTypes.isEmpty()) {
            return "";
        }
        return jobTypes.stream()
                .map(JobType::rocketPunchCode)
                .map(jobType -> config.getDelimiter() + JOB_TYPE_KEY + "=" + jobType)
                .collect(Collectors.joining());
    }

    private String toLocationParam(List<Location> locations) {
        if (locations.isEmpty()) {
            return "";
        }
        return locations.stream()
                .map(Location::rocketPunchCode)
                .map(location -> config.getDelimiter() + LOCATION_KEY + "=" + location)
                .collect(Collectors.joining());
    }

    private String toCareerParam(DetailedSearchDto.Career career) {
        StringBuilder params = new StringBuilder();

        if (ANY.equals(career.getCareerType())) {
            return "";
        }
        Optional.ofNullable(career.getCareerType())
                .ifPresent(careerType -> params.append(config.getDelimiter())
                                            .append(CAREER_TYPE_KEY + "=")
                                            .append(careerType.rocketPunchCode()));
        return params.toString();
    }

    private String toPayParam(DetailedSearchDto.Pay pay) {
        StringBuilder params = new StringBuilder(config.getDelimiter() + PAY_KEY + "=");

        if (pay.getPayMin() == null && pay.getPayMax() == null) {
            return "";
        }
        Integer payMin = Optional.ofNullable(pay.getPayMin()).orElse(0);
        Integer payMax = Optional.ofNullable(pay.getPayMax()).orElse(MAX_PAY);
        params.append(payMin * PAY_UNIT).append("-").append(payMax * PAY_UNIT);
        return params.toString();
    }

    private String toPageNumParam(int pageNum) {
        return config.getDelimiter() + PAGE_KEY + "=" + pageNum;
    }
}
