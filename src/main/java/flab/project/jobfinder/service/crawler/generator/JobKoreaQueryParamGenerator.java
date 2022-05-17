package flab.project.jobfinder.service.crawler.generator;

import flab.project.jobfinder.config.JobKoreaPropertiesConfig;
import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.enums.JobType;
import flab.project.jobfinder.enums.Location;
import lombok.RequiredArgsConstructor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JobKoreaQueryParamGenerator implements QueryParamGenerator {

    public static final String SEARCH_TEXT_KEY = "stext";
    public static final String JOBTYPE_KEY = "jobtype";
    public static final String LOCATION_KEY = "local";
    public static final String CAREER_TYPE_KEY = "careerType";
    public static final String CAREER_MIN_KEY = "careerMin";
    public static final String CAREER_MAX_KEY = "careerMax";
    public static final String PAY_TYPE_KEY = "payType";
    public static final String PAY_MIN_KEY = "payMin";
    public static final String PAY_MAX_KEY = "payMax";
    public static final String PAGE_KEY = "Page_No";
    public static final String TAB_TYPE_KEY = "tabType";
    private final JobKoreaPropertiesConfig config;

    @Override
    public String toQueryParams(DetailedSearchDto dto, int pageNum) {
        StringBuilder queryParams = new StringBuilder("tabType=recruit");
        StringBuilder queryParams = new StringBuilder(TAB_TYPE_KEY + "=recruit");
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
        String encoded = URLEncoder.encode(searchText, StandardCharsets.UTF_8);

        return "&" + SEARCH_TEXT_KEY + "=" + encoded;
    }

    private String toJobTypeParam(List<JobType> jobTypes) {
        if (jobTypes.isEmpty()) {
            return "";
        }
        String jobType = jobTypes.stream()
                .map(JobType::jobkoreaCode)
                .collect(Collectors.joining(config.getDelimiter()));
        return "&" + JOBTYPE_KEY + "=" + jobType;
    }

    private String toLocationParam(List<Location> locations) {
        if (locations.isEmpty()) {
            return "";
        }
        String location = locations.stream()
                .map(Location::jobKoreaCode)
                .collect(Collectors.joining(config.getDelimiter()));
        return "&" + LOCATION_KEY + "=" + location;
    }

    private String toCareerParam(DetailedSearchDto.Career career) {
        StringBuilder params = new StringBuilder();

        Optional.ofNullable(career.getCareerType())
                .ifPresent(careerType -> params.append("&" + CAREER_TYPE_KEY + "=").append(careerType.jobkoreaCode()));
        Optional.ofNullable(career.getCareerMin())
                .ifPresent(careerMin -> params.append("&" + CAREER_MIN_KEY + "=").append(careerMin));
        Optional.ofNullable(career.getCareerMax())
                .ifPresent(careerMax -> params.append("&" + CAREER_MAX_KEY + "=").append(careerMax));
        return params.toString();
    }

    private String toPayParam(DetailedSearchDto.Pay pay) {
        StringBuilder params = new StringBuilder();

        Optional.ofNullable(pay.getPayType())
                .ifPresent(payType -> params.append("&" + PAY_TYPE_KEY + "=").append(payType.jobkoreaCode()));
        Optional.ofNullable(pay.getPayMin())
                .ifPresent(payMin -> params.append("&" + PAY_MIN_KEY + "=").append(payMin));
        Optional.ofNullable(pay.getPayMax())
                .ifPresent(payMax -> params.append("&" + PAY_MAX_KEY + "=").append(payMax));
        return params.toString();
    }

    private String toPageNumParam(int pageNum) {
        return "&" + PAGE_KEY + "=" + pageNum;
    }
}
