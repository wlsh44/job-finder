package flab.project.jobfinder.dto;

import lombok.Builder;
import lombok.experimental.SuperBuilder;

import static flab.project.jobfinder.util.jobkorea.JobKoreaCrawlerServiceUtil.*;

@SuperBuilder
public class JobKoreaDetailedSearchDto extends DetailedSearchDto {

    @Override
    public String toQueryParams() {
        StringBuilder queryParams = new StringBuilder();
        String searchText = toSearchTextParam(this.searchText);
        String location = toLocationParam(this.location);
        String career = toCareerParam(this.career);
        String job = toJobTypeParam(this.jobType);
        String pay = toPayParam(this.pay);

        queryParams.append(searchText).append(location).append(career).append(job).append(pay);

        return queryParams.toString();
    }
}
