package flab.project.jobfinder.service.crawler.generator;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.enums.JobType;
import flab.project.jobfinder.enums.Location;
import flab.project.jobfinder.enums.jobkorea.JobKoreaCareerType;
import flab.project.jobfinder.enums.jobkorea.JobKoreaJobType;
import flab.project.jobfinder.enums.jobkorea.JobKoreaLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JobKoreaQueryParamGenerator implements QueryParamGenerator {

    public static final String SEARCH_TEXT_KEY = "stext";
    public static final String JOB_TYPE_KEY = "jobtype";
    public static final String LOCATION_KEY = "local";
    public static final String CAREER_TYPE_KEY = "careerType";
    public static final String CAREER_MIN_KEY = "careerMin";
    public static final String CAREER_MAX_KEY = "careerMax";
    public static final String PAY_TYPE_KEY = "payType";
    public static final String PAY_MIN_KEY = "payMin";
    public static final String PAY_MAX_KEY = "payMax";
    public static final String PAGE_KEY = "Page_No";
    public static final String TAB_TYPE_KEY = "tabType";

    @Override
    public MultiValueMap<String, String> toQueryParams(DetailedSearchDto dto, int pageNum) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(TAB_TYPE_KEY, "recruit");

        Optional.ofNullable(dto.getSearchText()).ifPresent(searchText -> queryParams.add(SEARCH_TEXT_KEY, searchText));
        Optional.ofNullable(dto.getLocation()).ifPresent(locations -> queryParams.addAll(LOCATION_KEY, toLocationParam(locations)));
        Optional.ofNullable(dto.getCareer()).ifPresent(career -> queryParams.addAll(toCareerParam(career)));
        Optional.ofNullable(dto.getJobType()).ifPresent(jobTypes -> queryParams.addAll(JOB_TYPE_KEY, toJobTypeParam(jobTypes)));
        Optional.ofNullable(dto.getPay()).ifPresent(pay -> queryParams.addAll(toPayParam(pay)));
        queryParams.add(PAGE_KEY, String.valueOf(pageNum));

        return queryParams;
    }

    private List<String> toJobTypeParam(List<JobType> jobTypes) {
        return jobTypes.stream()
                .map(jobType -> JobKoreaJobType.valueOf(jobType.name()).code())
                .collect(Collectors.toList());
    }

    private List<String> toLocationParam(List<Location> locations) {
        return locations.stream()
                .map(location -> JobKoreaLocation.valueOf(location.name()).code())
                .collect(Collectors.toList());
    }

    private MultiValueMap<String, String> toCareerParam(DetailedSearchDto.Career career) {
        MultiValueMap<String, String> careerParam = new LinkedMultiValueMap<>();

        Optional.ofNullable(career.getCareerType())
                .ifPresent(careerType -> careerParam.add(CAREER_TYPE_KEY, JobKoreaCareerType.valueOf(careerType.name()).code()));
        Optional.ofNullable(career.getCareerMin())
                .ifPresent(careerMin -> careerParam.add(CAREER_MIN_KEY, careerMin.toString()));
        Optional.ofNullable(career.getCareerMax())
                .ifPresent(careerMax -> careerParam.add(CAREER_MAX_KEY, careerMax.toString()));
        return careerParam;
    }

    private MultiValueMap<String, String> toPayParam(DetailedSearchDto.Pay pay) {
        MultiValueMap<String, String> payParam = new LinkedMultiValueMap<>();

        Optional.ofNullable(pay.getPayType())
                .ifPresent(payType -> payParam.add(PAY_TYPE_KEY, payType.jobkoreaCode()));
        Optional.ofNullable(pay.getPayMin())
                .ifPresent(payMin -> payParam.add(PAY_MIN_KEY, payMin.toString()));
        Optional.ofNullable(pay.getPayMax())
                .ifPresent(payMax -> payParam.add(PAY_MAX_KEY, payMax.toString()));
        return payParam;
    }
}
