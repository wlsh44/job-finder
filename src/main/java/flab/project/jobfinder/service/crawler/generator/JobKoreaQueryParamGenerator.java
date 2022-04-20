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

    private final JobKoreaPropertiesConfig config;

    @Override
    public String toQueryParams(DetailedSearchDto dto) {
        StringBuilder queryParams = new StringBuilder();
        String searchTextParam = Optional.ofNullable(dto.getSearchText()).map(this::toSearchTextParam).orElse("");
        String locationParam = Optional.ofNullable(dto.getLocation()).map(this::toLocationParam).orElse("");
        String careerParam = Optional.ofNullable(dto.getCareer()).map(this::toCareerParam).orElse("");
        String jobParam = Optional.ofNullable(dto.getJobType()).map(this::toJobTypeParam).orElse("");
        String payParam = Optional.ofNullable(dto.getPay()).map(this::toPayParam).orElse("");

        queryParams.append(searchTextParam).append(locationParam).append(careerParam).append(jobParam).append(payParam);

        return queryParams.toString();
    }

    private String toSearchTextParam(String searchText) {
        String encoded = URLEncoder.encode(searchText, StandardCharsets.UTF_8);

        return "stext=" + encoded;
    }

    private String toJobTypeParam(List<JobType> jobTypes) {
        String jobType = jobTypes.stream()
                .map(JobType::jobkoreaCode)
                .collect(Collectors.joining(config.getDelimiter()));
        return "&jobtype=" + jobType;
    }

    private String toLocationParam(List<Location> locations) {
        String location = locations.stream()
                .map(Location::jobkoreaCode)
                .collect(Collectors.joining(config.getDelimiter()));
        return "&local=" + location;
    }

    private String toCareerParam(DetailedSearchDto.Career career) {
        StringBuilder params = new StringBuilder();

        Optional.ofNullable(career.getCareerType()).ifPresent(x -> params.append("&careerType=").append(x.jobkoreaCode()));
        Optional.ofNullable(career.getCareerMin()).ifPresent(x -> params.append("&careerMin=").append(x));
        Optional.ofNullable(career.getCareerMax()).ifPresent(x -> params.append("&careerMax=").append(x));
        return params.toString();
    }

    private String toPayParam(DetailedSearchDto.Pay pay) {
        StringBuilder params = new StringBuilder();

        Optional.of(pay.getPayType()).ifPresent(x -> params.append("&payType=").append(x.jobkoreaCode()));
        Optional.ofNullable(pay.getPayMin()).ifPresent(x -> params.append("&payMin=").append(x));
        Optional.ofNullable(pay.getPayMax()).ifPresent(x -> params.append("&payMax=").append(x));
        return params.toString();
    }
}
