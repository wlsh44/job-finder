package flab.project.jobfinder.dto;

import flab.project.jobfinder.enums.CareerType;
import flab.project.jobfinder.enums.JobType;
import flab.project.jobfinder.enums.Location;
import flab.project.jobfinder.enums.PayType;
import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedSearchDto {

    private String searchText;
    private List<Location> location;
    private List<JobType> jobType;
    private Pay pay;
    private Career career;
    private String platform;

    @Data
    @NoArgsConstructor
    public static class Pay {
        private PayType payType;
        private String payMin;
        private String payMax;

        public Pay(PayType payType, String payMin, String payMax) {
            this.payType = payType;
            this.payMin = payMin;
            this.payMax = payMax;
        }
    }

    @Data
    @NoArgsConstructor
    public static class Career {
        private CareerType careerType;
        private String careerMin;
        private String careerMax;

        public Career(CareerType careerType, String careerMin, String careerMax) {
            this.careerType = careerType;
            this.careerMin = careerMin;
            this.careerMax = careerMax;
        }
    }
}
