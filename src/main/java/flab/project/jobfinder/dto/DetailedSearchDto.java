package flab.project.jobfinder.dto;

import flab.project.jobfinder.enums.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedSearchDto {

    private Platform platform;
    private String searchText;
    private List<Location> location;
    private List<JobType> jobType;
    private Pay pay;
    private Career career;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pay {
        private PayType payType;
        private Integer payMin;
        private Integer payMax;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Career {
        private CareerType careerType;
        private Integer careerMin;
        private Integer careerMax;
    }
}
