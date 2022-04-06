package flab.project.jobfinder.service.crawler;

import flab.project.jobfinder.dto.DetailedSearchDto;
import flab.project.jobfinder.util.JobType;
import flab.project.jobfinder.util.Location;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static flab.project.jobfinder.util.JobKoreaConst.JOBKOREA_DELIMITER;
import static flab.project.jobfinder.util.JobKoreaConst.JOBKOREA_URL;

@Slf4j
@Service
public class JobKoreaCrawlerService implements CrawlerService {

    @Override
    public Document crawling(DetailedSearchDto dto) {
        String url = JOBKOREA_URL + detailedSearchQueryParams(dto);

        try {
            Document doc = Jsoup.connect(url).get();

            return doc;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private String detailedSearchQueryParams(DetailedSearchDto dto) {
        StringBuilder queryParams = new StringBuilder();
        String searchText = getSearchText(dto);
        String location = getLocation(dto);
        String career = getCareer(dto);
        String job = getJobType(dto);
        String pay = getPay(dto);

        queryParams.append(searchText).append(location).append(career).append(job).append(pay);

        return queryParams.toString();
    }

    private String getSearchText(DetailedSearchDto dto) {
        if (dto.getSearchText() == null) {
            return "";
        }
        String encoded = URLEncoder.encode(dto.getSearchText(), StandardCharsets.UTF_8);

        return "stext=" + encoded;
    }

    private String getJobType(DetailedSearchDto dto) {
        if (dto.getJobType() == null) {
            return "";
        }
        String jobType = dto.getJobType().stream()
                .map(JobType::jobkoreaCode)
                .collect(Collectors.joining(JOBKOREA_DELIMITER));
        return "&jobtype=" + jobType;
    }

    private String getLocation(DetailedSearchDto dto) {
        if (dto.getLocation() == null) {
            return "";
        }
        String location = dto.getLocation().stream()
                .map(Location::jobkoreaCode)
                .collect(Collectors.joining(JOBKOREA_DELIMITER));
        return "&local=" + location;
    }

    private String getCareer(DetailedSearchDto dto) {
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

    private String getPay(DetailedSearchDto dto) {
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
