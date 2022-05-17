package flab.project.jobfinder.service.crawler.generator;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.enums.JobType;
import flab.project.jobfinder.enums.Location;
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
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(TAB_TYPE_KEY, "recruit");

        Optional.ofNullable(dto.getSearchText()).ifPresent(searchText -> map.add(SEARCH_TEXT_KEY, searchText));
        Optional.ofNullable(dto.getLocation()).ifPresent(locations -> map.addAll(LOCATION_KEY, toLocationParam(locations)));
        Optional.ofNullable(dto.getCareer()).ifPresent(career -> map.addAll(toCareerParam(career)));
        Optional.ofNullable(dto.getJobType()).ifPresent(jobTypes -> map.addAll(JOB_TYPE_KEY, toJobTypeParam(jobTypes)));
        Optional.ofNullable(dto.getPay()).ifPresent(pay -> map.addAll(toPayParam(pay)));
        map.add(PAGE_KEY, String.valueOf(pageNum));

        return map;
    }

    private List<String> toJobTypeParam(List<JobType> jobTypes) {
        return jobTypes.stream()
                .map(JobType::jobkoreaCode)
                .collect(Collectors.toList());
    }

    private List<String> toLocationParam(List<Location> locations) {
        return locations.stream()
                .map(Location::jobKoreaCode)
                .collect(Collectors.toList());
    }

    private MultiValueMap<String, String> toCareerParam(DetailedSearchDto.Career career) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        Optional.ofNullable(career.getCareerType())
                .ifPresent(careerType -> map.add(CAREER_TYPE_KEY, careerType.jobkoreaCode()));
        Optional.ofNullable(career.getCareerMin())
                .ifPresent(careerMin -> map.add(CAREER_MIN_KEY, careerMin.toString()));
        Optional.ofNullable(career.getCareerMax())
                .ifPresent(careerMax -> map.add(CAREER_MAX_KEY, careerMax.toString()));
        return map;
    }

    private MultiValueMap<String, String> toPayParam(DetailedSearchDto.Pay pay) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        Optional.ofNullable(pay.getPayType())
                .ifPresent(payType -> map.add(PAY_TYPE_KEY, payType.jobkoreaCode()));
        Optional.ofNullable(pay.getPayMin())
                .ifPresent(payMin -> map.add(PAY_MIN_KEY, payMin.toString()));
        Optional.ofNullable(pay.getPayMax())
                .ifPresent(payMax -> map.add(PAY_MAX_KEY, payMax.toString()));
        return map;
    }
}
