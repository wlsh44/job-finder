package flab.project.jobfinder.service.jobfind.crawler.generator;

import flab.project.jobfinder.dto.form.DetailedSearchDto;
import flab.project.jobfinder.enums.JobType;
import flab.project.jobfinder.enums.Location;
import flab.project.jobfinder.enums.rocketpunch.RocketPunchCareerType;
import flab.project.jobfinder.enums.rocketpunch.RocketPunchJobType;
import flab.project.jobfinder.enums.rocketpunch.RocketPunchLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static flab.project.jobfinder.enums.CareerType.ANY;
import static flab.project.jobfinder.enums.JobType.INTERN;

@RequiredArgsConstructor
public class RocketPunchQueryParamGenerator implements QueryParamGenerator {

    public static final int PAY_UNIT = 10000;
    public static final String JOB_TYPE_KEY = "hiring_types";
    public static final String LOCATION_KEY = "location";
    public static final String PAY_KEY = "salary";
    public static final String PAGE_KEY = "page";
    public static final String SEARCH_TEXT_KEY = "keywords";
    public static final String CAREER_TYPE_KEY = "career_type";

    @Override
    public MultiValueMap<String, String> toQueryParams(DetailedSearchDto dto, int pageNum) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

        Optional.ofNullable(dto.getSearchText())
                .ifPresent(searchText -> queryParams.add(SEARCH_TEXT_KEY, searchText));
        Optional.ofNullable(dto.getLocation())
                .ifPresent(locations -> queryParams.addAll(LOCATION_KEY, toLocationParam(locations)));
        Optional.ofNullable(dto.getCareer())
                .filter(career -> !ANY.equals(career.getCareerType()))
                .ifPresent(career -> queryParams.addAll(toCareerParam(career, dto.getJobType())));
        Optional.ofNullable(dto.getJobType())
                .ifPresent(jobTypes -> queryParams.addAll(JOB_TYPE_KEY, toJobTypeParam(jobTypes)));
        Optional.ofNullable(dto.getPay())
                .filter(pay -> !(pay.getPayMin() == null && pay.getPayMax() == null))
                .ifPresent(pay -> queryParams.add(PAY_KEY, toPayParam(pay)));
        queryParams.add(PAGE_KEY, String.valueOf(pageNum));

        return queryParams;
    }

    private List<String> toJobTypeParam(List<JobType> jobTypes) {
        return jobTypes.stream()
                .filter(jobType -> !INTERN.equals(jobType))
                .map(jobType -> RocketPunchJobType.valueOf(jobType.name()).code()).toList();
    }

    private List<String> toLocationParam(List<Location> locations) {
        return locations.stream()
                .map(location -> RocketPunchLocation.valueOf(location.name()).code())
                .collect(Collectors.toList());
    }

    private MultiValueMap<String, String> toCareerParam(DetailedSearchDto.Career career, List<JobType> jobTypes) {
        MultiValueMap<String, String> careerParam = new LinkedMultiValueMap<>();
        Optional.ofNullable(career.getCareerType())
                .ifPresent(careerType -> careerParam.add(CAREER_TYPE_KEY, RocketPunchCareerType.valueOf(careerType.name()).code()));
        if (jobTypes != null && jobTypes.contains(INTERN)) {
            careerParam.add(CAREER_TYPE_KEY, RocketPunchJobType.INTERN.code());
        }
        return careerParam;
    }

    private String toPayParam(DetailedSearchDto.Pay pay) {
        StringBuilder payParam = new StringBuilder();

        Integer payMin = Optional.ofNullable(pay.getPayMin()).orElse(0);
        Integer payMax = pay.getPayMax();
        payParam.append(payMin * PAY_UNIT).append("-");
        if (payMax != null) {
            payParam.append(payMax * PAY_UNIT);
        }
        return payParam.toString();
    }
}
