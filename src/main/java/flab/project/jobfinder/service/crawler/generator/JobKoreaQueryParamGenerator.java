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
    public String toQueryParams(DetailedSearchDto dto, int pageNum) {
        StringBuilder queryParams = new StringBuilder("tabType=recruit");
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

        return "&stext=" + encoded;
    }

    private String toJobTypeParam(List<JobType> jobTypes) {
        if (jobTypes.isEmpty()) {
            return "";
        }
        String jobType = jobTypes.stream()
                .map(JobType::jobkoreaCode)
                .collect(Collectors.joining(config.getDelimiter()));
        return "&jobtype=" + jobType;
    }

    private String toLocationParam(List<Location> locations) {
        if (locations.isEmpty()) {
            return "";
        }
        String location = locations.stream()
                .map(Location::jobkoreaCode)
                .collect(Collectors.joining(config.getDelimiter()));
        return "&local=" + location;
    }

    private String toCareerParam(DetailedSearchDto.Career career) {
        StringBuilder params = new StringBuilder();

        Optional.ofNullable(career.getCareerType())
                .ifPresent(careerType -> params.append("&careerType=").append(careerType.jobkoreaCode()));
        Optional.ofNullable(career.getCareerMin())
                .ifPresent(careerMin -> params.append("&careerMin=").append(careerMin));
        Optional.ofNullable(career.getCareerMax())
                .ifPresent(careerMax -> params.append("&careerMax=").append(careerMax));
        return params.toString();
    }

    private String toPayParam(DetailedSearchDto.Pay pay) {
        StringBuilder params = new StringBuilder();

        Optional.ofNullable(pay.getPayType())
                .ifPresent(payType -> params.append("&payType=").append(payType.jobkoreaCode()));
        Optional.ofNullable(pay.getPayMin())
                .ifPresent(payMin -> params.append("&payMin=").append(payMin));
        Optional.ofNullable(pay.getPayMax())
                .ifPresent(payMax -> params.append("&payMax=").append(payMax));
        return params.toString();
    }

    private String toPageNumParam(int pageNum) {
        return "&Page_No=" + pageNum;
    }
}
