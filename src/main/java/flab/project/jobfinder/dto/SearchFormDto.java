package flab.project.jobfinder.dto;

import flab.project.jobfinder.enums.*;
import flab.project.jobfinder.enums.location.District;
import flab.project.jobfinder.enums.location.LocationInterface;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class SearchFormDto {

    @NotNull(message = "플랫폼을 정해주세요")
    private Platform platform;
    private String searchText;
    private List<District> location;
    private List<JobType> jobType;
    private PayType payType;
    @Range(min = 0, max = 100000)
    private Integer payMin;
    @Range(min = 0, max = 100000)
    private Integer payMax;
    private CareerType careerType;
    @Min(0)
    private Integer careerMin;
    @Min(0)
    private Integer careerMax;

    @Min(1)
    @NotNull
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
