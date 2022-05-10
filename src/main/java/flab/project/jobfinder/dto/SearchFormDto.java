package flab.project.jobfinder.dto;

import flab.project.jobfinder.enums.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class SearchFormDto {

    @NotNull(message = "플랫폼을 정해주세요")
    private Platform platform;
    private String searchText;
    private List<Location> location;
    private List<JobType> jobType;
    private PayType payType;
    private Integer payMin;
    private Integer payMax;
    private CareerType careerType;
    private Integer careerMin;
    private Integer careerMax;

    @Min(1)
    private Integer currentPage;

    public DetailedSearchDto getDetailedSearchDto() {
        return DetailedSearchDto.builder()
                .platform(platform)
                .searchText(searchText)
                .location(location)
                .jobType(jobType)
                .pay(new DetailedSearchDto.Pay(payType, payMin, payMax))
                .career(new DetailedSearchDto.Career(careerType, careerMin, careerMax))
                .build();
    }
}
