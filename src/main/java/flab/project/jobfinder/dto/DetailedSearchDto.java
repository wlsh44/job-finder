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
        private final String minPay;
        private final String maxPay;

        public Pay(PayType payType, String minPay, String maxPay) {
            this.payType = payType;
            this.minPay = minPay;
            this.maxPay = maxPay;
        }
    }

    @Getter
    public static class Career {
        private final CareerType careerType;
        private final String minCareer;
        private final String maxCareer;

        public Career(CareerType careerType, String minCareer, String maxCareer) {
            this.careerType = careerType;
            this.minCareer = minCareer;
            this.maxCareer = maxCareer;
        }
    }
}
