package flab.project.jobfinder.dto;

import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

import static flab.project.jobfinder.util.jobkorea.JobKoreaCrawlerServiceUtil.*;

@SuperBuilder
public class JobKoreaDetailedSearchDto extends DetailedSearchDto {

    @Override
    public String toQueryParams() {
        StringBuilder queryParams = new StringBuilder();
        String searchTextParam = Optional.ofNullable(searchText).map(x ->toSearchTextParam(x)).orElse("");
        String locationParam = Optional.ofNullable(location).map(x -> toLocationParam(x)).orElse("");
        String careerParam = Optional.ofNullable(career).map(x -> toCareerParam(x)).orElse("");
        String jobParam = Optional.ofNullable(jobType).map(x -> toJobTypeParam(x)).orElse("");
        String payParam = Optional.ofNullable(pay).map(x -> toPayParam(x)).orElse("");

        queryParams.append(searchTextParam).append(locationParam).append(careerParam).append(jobParam).append(payParam);

        return queryParams.toString();
    }
}
