package flab.project.jobfinder.util.jobkorea;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.enums.JobType;
import flab.project.jobfinder.enums.Location;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static flab.project.jobfinder.util.jobkorea.JobKoreaConst.JOBKOREA_DELIMITER;

public class JobKoreaCrawlerServiceUtil {

    public static String toSearchTextParam(String searchText) {
        if (searchText == null) {
            return "";
        }
        String encoded = URLEncoder.encode(searchText, StandardCharsets.UTF_8);

        return "stext=" + encoded;
    }

    public static String toJobTypeParam(List<JobType> jobTypes) {
        if (jobTypes == null) {
            return "";
        }
        String jobType = jobTypes.stream()
                .map(JobType::jobkoreaCode)
                .collect(Collectors.joining(JOBKOREA_DELIMITER));
        return "&jobtype=" + jobType;
    }

    public static String toLocationParam(List<Location> locations) {
        if (locations == null) {
            return "";
        }
        String location = locations.stream()
                .map(Location::jobkoreaCode)
                .collect(Collectors.joining(JOBKOREA_DELIMITER));
        return "&local=" + location;
    }

    public static String toCareerParam(DetailedSearchDto.Career career) {
        if (career == null) {
            return "";
        }
        StringBuilder params = new StringBuilder();

        if (career.getCareerType() != null) {
            params.append("&careerType=").append(career.getCareerType().jobkoreaCode());
        }
        if (career.getCareerMin() != null) {
            params.append("&careerMin=").append(career.getCareerMin());
        }
        if (career.getCareerMax() != null) {
            params.append("&careerMax=").append(career.getCareerMax());
        }
        return params.toString();
    }

    public static String toPayParam(DetailedSearchDto.Pay pay) {
        if (pay == null) {
            return "";
        }
        StringBuilder params = new StringBuilder();

        params.append("&payType=").append(pay.getPayType().jobkoreaCode());
        if (pay.getPayMin() != null) {
            params.append("&payMin=").append(pay.getPayMin());
        }
        if (pay.getPayMax() != null) {
            params.append("&payMax=").append(pay.getPayMax());
        }
        return params.toString();
    }
}
