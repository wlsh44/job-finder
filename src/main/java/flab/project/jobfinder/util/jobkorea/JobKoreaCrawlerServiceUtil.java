package flab.project.jobfinder.util.jobkorea;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.enums.JobType;
import flab.project.jobfinder.enums.Location;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static flab.project.jobfinder.util.jobkorea.JobKoreaConst.JOBKOREA_DELIMITER;

public class JobKoreaCrawlerServiceUtil {

    public static String getSearchText(DetailedSearchDto dto) {
        if (dto.getSearchText() == null) {
            return "";
        }
        String encoded = URLEncoder.encode(dto.getSearchText(), StandardCharsets.UTF_8);

        return "stext=" + encoded;
    }

    public static String getJobType(DetailedSearchDto dto) {
        if (dto.getJobType() == null) {
            return "";
        }
        String jobType = dto.getJobType().stream()
                .map(JobType::jobkoreaCode)
                .collect(Collectors.joining(JOBKOREA_DELIMITER));
        return "&jobtype=" + jobType;
    }

    public static String getLocation(DetailedSearchDto dto) {
        if (dto.getLocation() == null) {
            return "";
        }
        String location = dto.getLocation().stream()
                .map(Location::jobkoreaCode)
                .collect(Collectors.joining(JOBKOREA_DELIMITER));
        return "&local=" + location;
    }

    public static String getCareer(DetailedSearchDto dto) {
        if (dto.getCareer() == null) {
            return "";
        }
        StringBuilder params = new StringBuilder();
        DetailedSearchDto.Career career = dto.getCareer();

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

    public static String getPay(DetailedSearchDto dto) {
        if (dto.getPay() == null) {
            return "";
        }
        StringBuilder params = new StringBuilder();
        DetailedSearchDto.Pay pay = dto.getPay();

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
