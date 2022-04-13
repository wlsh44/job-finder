package flab.project.jobfinder.util.jobkorea;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.enums.JobType;
import flab.project.jobfinder.enums.Location;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static flab.project.jobfinder.util.jobkorea.JobKoreaConst.JOBKOREA_DELIMITER;

public class JobKoreaCrawlerServiceUtil {

    public static String toSearchTextParam(String searchText) {
        String encoded = URLEncoder.encode(searchText, StandardCharsets.UTF_8);

        return "stext=" + encoded;
    }

    public static String toJobTypeParam(List<JobType> jobTypes) {
        String jobType = jobTypes.stream()
                .map(JobType::jobkoreaCode)
                .collect(Collectors.joining(JOBKOREA_DELIMITER));
        return "&jobtype=" + jobType;
    }

    public static String toLocationParam(List<Location> locations) {
        String location = locations.stream()
                .map(Location::jobkoreaCode)
                .collect(Collectors.joining(JOBKOREA_DELIMITER));
        return "&local=" + location;
    }

    public static String toCareerParam(DetailedSearchDto.Career career) {
        StringBuilder params = new StringBuilder();

        Optional.ofNullable(career.getCareerType()).ifPresent(x -> params.append("&careerType=").append(x.jobkoreaCode()));
        Optional.ofNullable(career.getCareerMin()).ifPresent(x -> params.append("&careerMin=").append(x));
        Optional.ofNullable(career.getCareerMax()).ifPresent(x -> params.append("&careerMax=").append(x));
        return params.toString();
    }

    public static String toPayParam(DetailedSearchDto.Pay pay) {
        StringBuilder params = new StringBuilder();

        Optional.of(pay.getPayType()).ifPresent(x -> params.append("&payType=").append(x.jobkoreaCode()));
        Optional.ofNullable(pay.getPayMin()).ifPresent(x -> params.append("&payMin=").append(x));
        Optional.ofNullable(pay.getPayMax()).ifPresent(x -> params.append("&payMax=").append(x));
        return params.toString();
    }
}
