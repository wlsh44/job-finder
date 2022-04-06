package flab.project.jobfinder.dto;

import flab.project.jobfinder.util.CareerType;
import flab.project.jobfinder.util.JobType;
import flab.project.jobfinder.util.Location;
import flab.project.jobfinder.util.PayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class DetailedSearchDto {

    private final String searchText;
    private final List<Location> location;
    private final List<JobType> jobType;
    private final Pay pay;
    private final Career career;

    @Getter
    public static class Pay {
        private final PayType payType;
        private final String payMin;
        private final String payMax;

        public Pay(PayType payType, String payMin, String payMax) {
            this.payType = payType;
            this.payMin = payMin;
            this.payMax = payMax;
        }
    }

    @Getter
    public static class Career {
        private final CareerType careerType;
        private final String careerMin;
        private final String careerMax;

        public Career(CareerType careerType, String careerMin, String careerMax) {
            this.careerType = careerType;
            this.careerMin = careerMin;
            this.careerMax = careerMax;
        }
    }
}
